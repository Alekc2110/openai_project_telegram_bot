package com.my.company.chatgpttelegrambot.api.openai;

import com.my.company.chatgpttelegrambot.domain.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.response.OpenAITextResponse;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
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
        ResponseEntity<OpenAITextResponse> responseEntity = httpClient.exchange(url, HttpMethod.POST, httpEntity, OpenAITextResponse.class);

        return responseEntity.getBody();
    }
}
