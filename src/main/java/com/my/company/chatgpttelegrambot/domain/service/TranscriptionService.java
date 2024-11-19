package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.api.openai.OpenAIClient;
import com.my.company.chatgpttelegrambot.domain.model.request.VoiceOpenAIRequest;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranscriptionService {

    private final OpenAIClient client;
    @NonNull
    public Response transcribeVoice(VoiceOpenAIRequest request, String url) {
        return client.sendVoiceToTranscription(request, url);
    }
}
