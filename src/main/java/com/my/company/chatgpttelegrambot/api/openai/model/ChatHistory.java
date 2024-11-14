package com.my.company.chatgpttelegrambot.api.openai.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatHistory (List<Message> chatMessages){}

