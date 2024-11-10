package com.my.company.chatgpttelegrambot.api.openai.config;

import com.my.company.chatgpttelegrambot.api.openai.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfiguration {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    @Bean
    public OpenAIClient openAIClient(@Value("${openai.token}")
                                         String openAiToken,
                                         RestTemplate restTemplate){
        return new OpenAIClient(openAiToken, restTemplate);
    }
}
