package com.my.company.chatgpttelegrambot.domain.bot.handler.commandhandler;

import com.my.company.chatgpttelegrambot.domain.model.TelegramCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramCommandHandler {

    BotApiMethod<?> processCommand(Update update);
    TelegramCommand getSupportedCommand();
}
