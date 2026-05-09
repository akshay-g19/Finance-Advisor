package com.example.financeadvisor.config;


import org.springframework.ai.chat.client.ChatClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    @Primary
    @Bean ChatClient chatClient(ChatClient.Builder builder){
        return builder.build();
    }
}