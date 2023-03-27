package com.example.hive.service.implementation;

import com.example.hive.dto.request.PayStackPaymentRequest;
import com.example.hive.dto.response.PayStackResponse;
import com.example.hive.dto.response.VerifyTransactionResponse;
import com.example.hive.entity.User;
import com.example.hive.exceptions.CustomException;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.EmailService;
import com.example.hive.service.PayStackService;
import com.example.hive.utils.AuthDetails;
import com.example.hive.utils.EmailTemplates;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.HashMap;

@AllArgsConstructor
@Service
@NoArgsConstructor
@Slf4j
public class PayStackImpl implements PayStackService {

    @Value("${secret.key}")
    private String PAY_STACK_SECRET_KEY;


    @Value("${paystack.url}")
    private String PAY_STACK_BASE_URL;

    @Value("${paystack.verification.url}")
    private String PAY_STACK_VERIFY_URL;

    private AuthDetails authDetails;
    private EmailService emailService;



    private HashMap<String, String> paymentTracker;

    @Autowired
    public PayStackImpl(AuthDetails authDetails, EmailService emailService) {
        this.authDetails = authDetails;
        this.emailService = emailService;
    }

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

        if (paymentTracker == null) {
            paymentTracker = new HashMap<>();
        }

        String reference = initializeTransactionResponse.getData().getReference();
        paymentTracker.put(reference, user.getEmail());

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

}
