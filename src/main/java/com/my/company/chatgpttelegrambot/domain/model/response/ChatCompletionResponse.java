package com.my.company.chatgpttelegrambot.domain.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatCompletionResponse(@JsonProperty("choices") List<Choice> choices) implements Response {
    @Override
    public String getContent() {
        return choices.get(0).message().content();
    }
}
