package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.domain.model.ChatHistory;
import com.my.company.chatgpttelegrambot.domain.model.DataType;
import com.my.company.chatgpttelegrambot.domain.model.Message;
import com.my.company.chatgpttelegrambot.domain.model.OpenAIModel;
import com.my.company.chatgpttelegrambot.domain.model.request.TextOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.request.VoiceOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.File;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGptModelStrategy {

    @Value("${openai.textRemoteUrl}")
    private String textRemoteUrl;
    @Value("${openai.voiceTranscriptionRemoteUrl}")
    private String voiceTranscriptionRemoteUrl;

    private final ChatGptService gptService;
    private final ChatGptHistoryCache textContentCache;
    private final TranscriptionService transcriptionService;

    public Optional<Response> getOpenAIResponse(Long userId, Object input, DataType dataType) {
        return switch (input) {
            case String text when dataType.equals(DataType.TEXT)-> {
                var openAIRequest = createTextRequestForOpenAI(userId, text);
                var response = gptService.getResponseChatForUser(openAIRequest, textRemoteUrl);
                String content = response.getContent();
                createMessageCache(userId, Message.builder()
                        .content(content)
                        .role("user")
                        .build());
                yield Optional.of(response);
            }
            case File file when dataType.equals(DataType.VOICE) -> {
                var voiceOpenAIRequest = createVoiceRequestForTranscriptionOpenAI(file);
                Response response = transcriptionService.transcribeVoice(voiceOpenAIRequest, voiceTranscriptionRemoteUrl);
                yield Optional.of(response);
            }
            default -> {
                log.error("incompatible input type: {} used in ChatGptModelStrategy", input);
                yield Optional.empty();
            }
        };
    }

    private TextOpenAIRequest createTextRequestForOpenAI(Long userId, String textInput) {
        var history = createMessageCache(userId,
                Message.builder()
                        .content(textInput)
                        .role("user")
                        .build());
        return TextOpenAIRequest.builder()
                .model(OpenAIModel.GPT_4O_MINI.getModelName())
                .messages(history.chatMessages())
                .temperature(0.7f)
                .build();
    }

    private VoiceOpenAIRequest createVoiceRequestForTranscriptionOpenAI(File file) {
        return VoiceOpenAIRequest.builder()
                .model(OpenAIModel.WHISPER_1.getModelName())
                .audioFile(file)
                .build();
    }

    private ChatHistory createMessageCache(Long userId, Message message) {
        textContentCache.creatHistoryIfNotExist(userId);
        return textContentCache.addMessageToHistory(userId, message);
    }
}
