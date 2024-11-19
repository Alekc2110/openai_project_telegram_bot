package com.my.company.chatgpttelegrambot.api.openai;

import com.my.company.chatgpttelegrambot.domain.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.request.VoiceOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.response.OpenAITextResponse;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import com.my.company.chatgpttelegrambot.domain.model.response.SimpleTextResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        headers.set("Content-Type", "application/json");



        HttpEntity<TextOpenAIRequest> httpEntity = new HttpEntity<>(request, headers);
        log.info("created openApi request for model : {}", request.model());
        ResponseEntity<OpenAITextResponse> responseEntity = httpClient.exchange(url, HttpMethod.POST, httpEntity, OpenAITextResponse.class);

        return responseEntity.getBody();
    }

    public Response sendVoiceToTranscription(VoiceOpenAIRequest request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiToken);
        headers.set("Content-Type", "multipart/form-data");


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(request.audioFile()));
        body.add("model", request.model());


        var httpEntity = new HttpEntity<>(body, headers);
        log.info("created openApi request for model : {}", request.model());
        ResponseEntity<SimpleTextResponse> responseEntity = httpClient.exchange(url, HttpMethod.POST, httpEntity, SimpleTextResponse.class);

        return responseEntity.getBody();
    }
}
