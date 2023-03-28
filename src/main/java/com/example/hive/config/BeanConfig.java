package com.example.hive.config;

import com.example.hive.security.WebClientHandler;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;


@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    @Bean
    public HttpClient httpClient(){
        return HttpClient.newHttpClient();
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }

    @Bean
    public WebClientHandler webClientHandler(){
        return new WebClientHandler(httpClient(), gson());
    }


}
