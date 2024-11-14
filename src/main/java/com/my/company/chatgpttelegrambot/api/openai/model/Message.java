package com.my.company.chatgpttelegrambot.api.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Message(
       @JsonProperty("role") String role,
       @JsonProperty("content") String content
) {}
