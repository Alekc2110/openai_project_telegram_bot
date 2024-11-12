package com.my.company.chatgpttelegrambot.api.openai.model;

public record SimpleTextResponse (String text) implements Response {
    @Override
    public String getContent() {
        return text;
    }
}
