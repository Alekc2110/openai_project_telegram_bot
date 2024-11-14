package com.my.company.chatgpttelegrambot.api.openai.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OpenAIModel {
    GPT_4O_MINI("gpt-4o-mini"),
    O1_MINI("o1-mini"),
    O1_PREVIEW("o1-preview"),
    GPT_4O("gpt-4o"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    WHISPER_1("whisper-1"),
    DALLE_E_2("dall-e-2");

    private final String modelName;

}
