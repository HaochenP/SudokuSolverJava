package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


public class Appconfig {
    public WebClient webClient() {
        return WebClient.builder().build();
    }

}
