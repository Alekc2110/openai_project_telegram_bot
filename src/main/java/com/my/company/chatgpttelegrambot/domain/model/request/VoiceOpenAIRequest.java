package com.my.company.chatgpttelegrambot.domain.model.request;

import lombok.Builder;

import java.io.File;

@Builder
public record VoiceOpenAIRequest(File audioFile, String model) {
}
