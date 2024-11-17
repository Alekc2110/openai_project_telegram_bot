package com.my.company.chatgpttelegrambot.domain.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.company.chatgpttelegrambot.domain.model.Message;

public record Choice(@JsonProperty("message") Message message) {
}
