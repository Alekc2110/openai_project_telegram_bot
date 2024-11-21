package com.my.company.chatgpttelegrambot.domain.bot.handler.commandhandler;

import com.my.company.chatgpttelegrambot.domain.model.TelegramCommand;
import com.my.company.chatgpttelegrambot.domain.model.response.SimpleTextResponse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartCommandHandler implements TelegramCommandHandler {
    private final String HELLO_MESSAGE = """
            "Hello %s, this bot for using OpenAI technologies. let`s get started!"
            """;
    @Override
    public BotApiMethod<?> processCommand(Message message) {
        SimpleTextResponse response = new SimpleTextResponse(HELLO_MESSAGE.formatted(message.getChat().getFirstName()));
        return new SendMessage(message.getChatId().toString(), response.getContent());
    }

    @Override
    public TelegramCommand getSupportedCommand() {
        return TelegramCommand.START_COMMAND;
    }
}
