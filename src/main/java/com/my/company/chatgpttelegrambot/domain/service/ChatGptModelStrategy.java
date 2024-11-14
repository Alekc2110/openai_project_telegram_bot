package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.api.openai.model.DataType;
import com.my.company.chatgpttelegrambot.api.openai.model.Message;
import com.my.company.chatgpttelegrambot.api.openai.model.OpenAIModel;
import com.my.company.chatgpttelegrambot.api.openai.model.request.OpenAIRequest;
import com.my.company.chatgpttelegrambot.api.openai.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.api.openai.model.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatGptModelStrategy {
    private final ChatGptService service;

    public Response getOpenAIResponse(Long userId, String textInput, DataType dataType) {
        return switch (dataType) {
            case TEXT -> {
                OpenAIRequest openAIRequest = createRequestForOpenAI(userId, textInput, dataType);
                yield service.getTextResponseChatForUser(userId, openAIRequest);
            }
        };
    }

    private OpenAIRequest createRequestForOpenAI(Long userId, String textInput, DataType dataType){
      return switch (dataType){
            case TEXT -> {
               yield new TextOpenAIRequest(
                        OpenAIModel.GPT_4O_MINI.getModelName(),
                        List.of(Message.builder()
                                .content(textInput)
                                .role("user")
                                .build()),
                        0.5f
                );
            }
        };
    }
}
