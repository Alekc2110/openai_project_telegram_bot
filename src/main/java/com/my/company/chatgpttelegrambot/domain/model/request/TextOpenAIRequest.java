package com.my.company.chatgpttelegrambot.domain.model.request;

import com.my.company.chatgpttelegrambot.domain.model.Message;
import lombok.*;

import java.util.List;

@Builder
public record TextOpenAIRequest(String model, List<Message> messages, Float temperature) {
}


