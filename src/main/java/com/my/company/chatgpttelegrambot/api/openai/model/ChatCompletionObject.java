package com.my.company.chatgpttelegrambot.api.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatCompletionObject(@JsonProperty("choices") List<Choice> choices) implements Response {
    @Override
    public String getContent() {
        return choices.get(0).message().content();
    }
}
