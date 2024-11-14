package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.api.openai.OpenAIClient;
import com.my.company.chatgpttelegrambot.api.openai.model.Message;
import com.my.company.chatgpttelegrambot.api.openai.model.OpenAIModel;
import com.my.company.chatgpttelegrambot.api.openai.model.request.OpenAIRequest;
import com.my.company.chatgpttelegrambot.api.openai.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.api.openai.model.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    @Value("${openai.textRemoteUrl}")
    private String textRemoteUrl;

    private final OpenAIClient client;

    public Response getTextResponseChatForUser(Long userId, OpenAIRequest request) {

        return client.sendRequest(request, textRemoteUrl);
    }
}
