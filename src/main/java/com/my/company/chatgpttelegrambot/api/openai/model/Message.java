package com.my.company.chatgpttelegrambot.api.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Message(
       @JsonProperty("role") String role,
       @JsonProperty("content") String content
) {}
