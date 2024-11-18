package com.my.company.chatgpttelegrambot.domain.model;

import lombok.Getter;

@Getter
public enum TelegramCommand {
    START_COMMAND("/start"),
    NOT_A_COMMAND("");

    private final String commandName;

    TelegramCommand(String commandName) {
        this.commandName = commandName;
    }
}
