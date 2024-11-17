package com.my.company.chatgpttelegrambot.domain.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatHistory (List<Message> chatMessages){}

