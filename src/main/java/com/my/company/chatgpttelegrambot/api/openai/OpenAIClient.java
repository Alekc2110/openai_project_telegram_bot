package com.my.company.chatgpttelegrambot.api.openai;

import com.my.company.chatgpttelegrambot.api.openai.model.ChatCompletionObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
@Slf4j
public class OpenAIClient {
    private final String token;
    private final RestTemplate client;
    @Value("${bot.model}")
    private String chatGptModel;
    @Value("${openai.remoteUrl}")
    private String url;

    public OpenAIClient(String token, RestTemplate client) {
        this.token = token;
        this.client = client;
    }

    public ChatCompletionObject createChatCompletion(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-type", "application/json");

        String request = """
                {
                     "model": "%s",
                     "messages": [{
                                  "role": "user",
                                  "content": "%s"
                                  }],
                     "temperature": 0.5
                   }
                """.formatted(chatGptModel, message);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
        log.info("created request for openApi: {}", request);
        ResponseEntity<ChatCompletionObject> responseEntity = client.exchange(url, HttpMethod.POST, httpEntity, ChatCompletionObject.class);

        return responseEntity.getBody();
    }
}
