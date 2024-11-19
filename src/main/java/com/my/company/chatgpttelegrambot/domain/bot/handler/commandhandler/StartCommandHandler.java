package com.my.company.chatgpttelegrambot.domain.bot.handler.commandhandler;

import com.my.company.chatgpttelegrambot.domain.model.TelegramCommand;
import com.my.company.chatgpttelegrambot.domain.model.response.SimpleTextResponse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommandHandler implements TelegramCommandHandler {
    private final String HELLO_MESSAGE = """
            "Hello %s, this bot for using OpenAI technologies. let`s get started!"
            """;
    @Override
    public BotApiMethod<?> processCommand(Update update) {
        SimpleTextResponse response = new SimpleTextResponse(HELLO_MESSAGE.formatted(update.getMessage().getChat().getFirstName()));
        return new SendMessage(update.getMessage().getChatId().toString(), response.getContent());
    }

    @Override
    public TelegramCommand getSupportedCommand() {
        return TelegramCommand.START_COMMAND;
    }
}
