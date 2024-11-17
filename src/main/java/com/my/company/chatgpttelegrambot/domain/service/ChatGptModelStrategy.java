package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.domain.model.ChatHistory;
import com.my.company.chatgpttelegrambot.domain.model.DataType;
import com.my.company.chatgpttelegrambot.domain.model.Message;
import com.my.company.chatgpttelegrambot.domain.model.OpenAIModel;
import com.my.company.chatgpttelegrambot.domain.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatGptModelStrategy {

    @Value("${openai.textRemoteUrl}")
    private String textRemoteUrl;

    private final ChatGptService service;
    private final ChatGptHistoryCache textContentCache;

    public Response getOpenAIResponse(Long userId, String textInput, DataType dataType) {
        return switch (dataType) {
            case TEXT -> {
                TextOpenAIRequest openAIRequest = createTextRequestForOpenAI(userId, textInput);
                var response = service.getResponseChatForUser(openAIRequest, textRemoteUrl);
                String content = response.getContent();
                createMessageCache(userId, Message.builder()
                        .content(content)
                        .role("user")
                        .build());
                yield response;
            }
        };
    }

    private TextOpenAIRequest createTextRequestForOpenAI(Long userId, String textInput) {
        var history = createMessageCache(userId, Message.builder()
                .content(textInput)
                .role("user")
                .build());
        return new TextOpenAIRequest(
                OpenAIModel.GPT_4O_MINI.getModelName(),
                history.chatMessages(),
                0.7f
        );
    }

    private ChatHistory createMessageCache(Long userId, Message message) {
        textContentCache.creatHistoryIfNotExist(userId);
        return textContentCache.addMessageToHistory(userId, message);
    }
}
