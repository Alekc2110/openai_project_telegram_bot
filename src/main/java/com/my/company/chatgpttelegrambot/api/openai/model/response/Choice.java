package com.my.company.chatgpttelegrambot.api.openai.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.company.chatgpttelegrambot.api.openai.model.Message;

public record Choice(@JsonProperty("message") Message message) {
}
