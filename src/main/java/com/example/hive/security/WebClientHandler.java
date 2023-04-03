package com.example.hive.security;

import com.example.hive.dto.request.ValidateAccountDto;
import com.example.hive.dto.response.ApiResponse;
import com.example.hive.dto.response.ListBanksResponse;
import com.example.hive.dto.response.ValidateAccountResponse;
import com.example.hive.exceptions.CustomException;
import com.example.hive.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;



@RequiredArgsConstructor
@Slf4j
public class WebClientHandler {

//    private final HttpClient client ;
//    private final Gson gson;
//
//    public List<ListBanksResponse> getListBanksResponses(HttpRequest request) {
//        try {
//            var response = getBaseResponseCompletableFuture(request)
//                    .thenApply(ApiResponse::getStatus)
//                    .get();
//
//            return gson.fromJson(gson.toJson(response), new TypeToken<List<ListBanksResponse>>() {
//            }.getType());
//        } catch (InterruptedException | ExecutionException e) {
//            Thread.currentThread().interrupt();
//        }
//        return Collections.emptyList();
//    }
//
//    public ApiResponse processResponse(HttpRequest request) {
//        try {
//            return getBaseResponseCompletableFuture(request)
//                    .get();
//        } catch (InterruptedException | ExecutionException e) {
//            Thread.currentThread().interrupt();
//        }
//        return null;
//    }
//
//    public CompletableFuture<ApiResponse> getBaseResponseCompletableFuture(HttpRequest request) {
//        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::body)
//                .thenApply(WebClientHandler::mapToBaseResponse);
//    }
//
//    private static ApiResponse mapToBaseResponse(String x) {
//        try {
//            return getMapper().readValue(x, ApiResponse.class);
//        } catch (JsonProcessingException e) {
//            throw new CustomException(e.getMessage());
//        }
//    }
//
//    public ValidateAccountResponse processValidateAccountResponse(ValidateAccountDto validateAccountDto, HttpRequest request, String bankName) {
//        return getBaseResponseCompletableFuture(request)
//                .thenApply(body -> {
//                    if (body.getData() != null) {
//                        var res = gson.fromJson(gson.toJson(body.getData()), ValidateAccountResponse.class);
//                        res.setBank_code(validateAccountDto.getCode());
//                        res.setBank_name(bankName);
//                        return res;
//                    } else {
//                        throw new InvalidRequestException(body.getMessage());
//                    }
//                }).join();
//    }
//
//    public String processFieldValue(HttpRequest request, String field){
//        return getBaseResponseCompletableFuture(request)
//                .thenApply(responseBody -> {
//                    if (responseBody.getData() != null) {
//                        JsonObject data = gson.toJsonTree(responseBody.getData()).getAsJsonObject();
//                        log.info("JsonObject data: {}", data);
//                        return data.get(field).getAsString();
//                    } else {
//                        return responseBody.getMessage();
//                    }
//                }).join();
//
//    }
}
