package com.my.company.chatgpttelegrambot.domain.model.response;

public record SimpleTextResponse (String text) implements Response {
    @Override
    public String getContent() {
        return text;
    }
}
