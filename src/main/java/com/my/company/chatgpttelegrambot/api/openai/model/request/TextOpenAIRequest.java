package com.my.company.chatgpttelegrambot.api.openai.model.request;

import com.my.company.chatgpttelegrambot.api.openai.model.Message;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class TextOpenAIRequest extends OpenAIRequest {
    private final List<Message> messages;
    private final Float temperature;

    public TextOpenAIRequest(String model, List<Message> messages, Float temperature) {
        super(model);
        this.messages = messages;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return """
                {
                "model": "%s",
                "messages": [{
                            "role": "user",
                            "content": "%s"
                            }],
                "temperature": %f
                }
                """.formatted(super.getModel(), messages, temperature);
    }
}


