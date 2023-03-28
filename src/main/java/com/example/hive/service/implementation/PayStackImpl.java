package com.example.hive.service.implementation;

import com.example.hive.constant.TransactionStatus;
import com.example.hive.constant.TransactionType;
import com.example.hive.dto.Request.*;
import com.example.hive.dto.response.*;
import com.example.hive.entity.TransactionLog;
import com.example.hive.entity.User;
import com.example.hive.exceptions.CustomException;
import com.example.hive.exceptions.InvalidRequestException;
import com.example.hive.repository.TransactionRepository;
import com.example.hive.security.WebClientHandler;
import com.example.hive.service.EmailService;
import com.example.hive.service.PayStackService;
import com.example.hive.utils.AuthDetails;
import com.example.hive.utils.EmailTemplates;
import com.example.hive.utils.TransactionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import static com.example.hive.constant.AppConstants.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.hive.utils.TransactionUtil.convertToJsonBody;
import static com.example.hive.utils.TransactionUtil.getAuthHeader;

@Service
@RequiredArgsConstructor
public class PayStackImpl implements PayStackService {

    @Value("${secret.key}")
    private  String PAY_STACK_SECRET_KEY;

    @Value("${paystack.url}")
    private  String PAY_STACK_BASE_URL;

    @Value("${paystack.verification.url}")
    private  String PAY_STACK_VERIFY_URL;

    private final AuthDetails authDetails;
    private final EmailService emailService;

    private final WebClientHandler webClientHandler;

    private final Gson gson;
    private final TransactionRepository transactionRepository;


    @Override
    public PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception {
        PayStackResponse initializeTransactionResponse;

        if (request.getAmount() <= 0) {
            throw new CustomException("Deposit must be greater than zero");
        }

        User user = authDetails.getAuthorizedUser(principal);

        if (user != null) {
            request.setEmail(user.getEmail());
        } else {
            throw new CustomException("No authenticated user found");
        }

        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(request));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(PAY_STACK_BASE_URL);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + PAY_STACK_SECRET_KEY);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new AuthenticationException("Error Occurred while initializing transaction");
            }
            ObjectMapper mapper = new ObjectMapper();

            initializeTransactionResponse = mapper.readValue(result.toString(), PayStackResponse.class);
        } catch (Exception ex) {
            throw new Exception("Failure initializing payStack transaction");
        }

        String reference = initializeTransactionResponse.getData().getReference();


        emailService.sendEmail(EmailTemplates.createPaymentVerificationCodeMail(user, reference));

        return initializeTransactionResponse;
    }

    @Override
    public VerifyTransactionResponse verifyPayment(String reference) {


        VerifyTransactionResponse payStackResponse;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(PAY_STACK_VERIFY_URL + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + PAY_STACK_SECRET_KEY);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new CustomException("Error Occurred while connecting to PayStack URL");
            }
            ObjectMapper mapper = new ObjectMapper();


            payStackResponse = mapper.readValue(result.toString(), VerifyTransactionResponse.class);

//            walletService.fundWallet(email, payStackResponse, "My new wallet",
//                    "Wallet funded from payStack");

//            paymentTracker.remove(reference);

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        return payStackResponse;
    }




    // create a method for getting banks

    @Override
    public List<ListBanksResponse> fetchBanks() {

        HttpRequest request = HttpRequest.newBuilder()
                .headers(getAuthHeader(PAY_STACK_SECRET_KEY))
                .uri(URI.create(String.format("%s%s", PSTK_BASE_URI, PSTK_LIST_BANKS_URI)))
                .build();

        return webClientHandler.getListBanksResponses(request);
    }


    // verifying account details



    @Override
    public ValidateAccountResponse validateBankAccount(ValidateAccountDto validateAccountDto, String bankName) {

        HttpRequest request = HttpRequest.newBuilder()
                .headers(getAuthHeader(PAY_STACK_SECRET_KEY))
                .uri(URI.create(String.format("%s%s", PSTK_BASE_URI, PSTK_VALIDATE_BANK_URI)
                        .concat("?account_number=")
                        .concat(validateAccountDto.getAccountNumber())
                        .concat("&bank_code=")
                        .concat(validateAccountDto.getCode())))
                .build();

        return webClientHandler.processValidateAccountResponse(validateAccountDto, request, bankName);
    }
    @Override
    public TransferResponse transferFunds(BankTransferDto bankTransferDto, User user) {

        var recipient = createRecipient(
                bankTransferDto.getBeneficiaryAccountNumber(),
                bankTransferDto.getBeneficiaryBankCode()
        );

        String recipientCode = getRecipientCode(recipient);

        PayStackTransferRequest transferRequest = PayStackTransferRequest.builder()
                .amount(bankTransferDto.getAmount())
                .recipient(recipientCode)
                .reason(bankTransferDto.getNarration())
                .reference(StringUtils.isBlank(bankTransferDto.getTransactionReference()) ? TransactionUtil.generateUniqueRef() : bankTransferDto.getTransactionReference())
                .build();

        String jsonBody = convertToJsonBody(transferRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .headers(getAuthHeader(PAY_STACK_SECRET_KEY))
                .uri(URI.create(String.format("%s%s", PSTK_BASE_URI, PSTK_TRANSFER_URI)))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        PayStackTransferResponse response = webClientHandler.getBaseResponseCompletableFuture(request)
                .thenApply(responseBody -> {
                    if (responseBody.getData() != null) {
                        return gson.fromJson(gson.toJson(responseBody.getData()), PayStackTransferResponse.class);
                    } else {
                        throw new InvalidRequestException(responseBody.getMessage());
                    }
                }).join();

        transactionRepository.save(mapToTransactionLogEntity(response, user));
        return mapToTransferResponse(response);
    }

    @Override
    public Optional<String> getTransactionStatusValue(String transactionReference) {
        HttpRequest request = HttpRequest.newBuilder()
                .headers(getAuthHeader(PAY_STACK_SECRET_KEY))
                .uri(URI.create(String.format("%s%s", PSTK_BASE_URI, PSTK_VERIFY_TRANSACTION_STATUS_URI)
                        .concat(transactionReference)))
                .build();

        return Optional.ofNullable(webClientHandler.processFieldValue(request, "status"));
    }


    private TransferResponse mapToTransferResponse(PayStackTransferResponse response) {
        return TransferResponse.builder()
                .amount(response.getAmount().toString())
                .beneficiaryAccountNumber(response.getAccount_number())
                .beneficiaryAccountName(response.getName())
                .beneficiaryBankCode(response.getBank_code())
                .transactionReference(response.getReference())
                .transactionDateTime(response.getTransfer_date())
                .currencyCode(response.getCurrency())
                .responseMessage(response.getMessage())
                .status(response.getStatus())
                .build();
    }

    private String getRecipientCode(Object recipient) {
        return gson.fromJson(gson.toJson(recipient), JsonObject.class)
                .get("recipient_code").getAsString();
    }

    private Object createRecipient(String recipientAccountNumber, String recipientBankCode) {

        PayStackTransferRecepientRequest recipientRequest = PayStackTransferRecepientRequest.builder()
                .accountNumber(recipientAccountNumber)
                .bankCode(recipientBankCode)
                .type("nuban")
                .build();

        String jsonBody = convertToJsonBody(recipientRequest);
        HttpRequest request = HttpRequest.newBuilder()
                .headers(getAuthHeader(PAY_STACK_SECRET_KEY))
                .uri(URI.create(String.format("%s%s", PSTK_BASE_URI, PSTK_TRANSFER_RECIPIENT_URI)))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        var response = webClientHandler.processResponse(request);

        if (Objects.nonNull(response)) {
            if (Objects.equals(response.getStatusCode().getReasonPhrase(), "true")) {
                return response.getData();
            } else {
                throw new InvalidRequestException(response.getMessage());
            }
        }
        return null;
    }

    private TransactionLog mapToTransactionLogEntity(PayStackTransferResponse response, User user) {
        //TODO add looged in doer
        return TransactionLog.builder()
                .doerWithdrawer(user)
                .amount(response.getAmount())
                .paystackReference(response.getReference())
                .transactionType(TransactionType.WITHDRAW)
                .transactionDate(response.getTransfer_date())
                .transactionStatus(response.getStatus().equalsIgnoreCase("success")
                        ? TransactionStatus.SUCCESS
                        : response.getStatus().equalsIgnoreCase("failure")
                        ? TransactionStatus.FAILED
                        : TransactionStatus.PENDING)
                .build();
    }
}




