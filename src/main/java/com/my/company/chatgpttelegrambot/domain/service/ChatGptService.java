package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.api.openai.OpenAIClient;
import com.my.company.chatgpttelegrambot.domain.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final OpenAIClient client;

    @NonNull
    public Response getResponseChatForUser(TextOpenAIRequest request, String url) {
        return client.sendRequest(request, url);
    }
}
