package com.my.company.chatgpttelegrambot.api.openai;

import com.my.company.chatgpttelegrambot.api.openai.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.api.openai.model.response.ChatCompletionResponse;
import com.my.company.chatgpttelegrambot.api.openai.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
@Slf4j
public class OpenAIClient {
    private final String openAiToken;
    private final RestTemplate httpClient;

    public OpenAIClient(String token, RestTemplate client) {
        this.openAiToken = token;
        this.httpClient = client;
    }

    public Response sendRequest(TextOpenAIRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiToken);
        headers.set("Content-type", "application/json");



        HttpEntity<TextOpenAIRequest> httpEntity = new HttpEntity<>(request, headers);
        log.info("created request for openApi: {}", request);
        ResponseEntity<ChatCompletionResponse> responseEntity = httpClient.exchange(url, HttpMethod.POST, httpEntity, ChatCompletionResponse.class);

        return responseEntity.getBody();
    }
}
