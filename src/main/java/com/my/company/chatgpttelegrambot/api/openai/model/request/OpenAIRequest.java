package com.my.company.chatgpttelegrambot.api.openai.model.request;

import lombok.*;

@Getter
@Setter
public abstract class OpenAIRequest {

    private final String model;
    public OpenAIRequest(String model) {
        this.model = model;
    }

}
