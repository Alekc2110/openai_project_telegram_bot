package com.my.company.chatgpttelegrambot.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Message(
       @JsonProperty("role") String role,
       @JsonProperty("content") String content
) {}
