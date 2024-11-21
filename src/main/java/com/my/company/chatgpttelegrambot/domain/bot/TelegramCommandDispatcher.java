package com.my.company.chatgpttelegrambot.domain.bot;

import com.my.company.chatgpttelegrambot.domain.bot.handler.commandhandler.TelegramCommandHandler;
import com.my.company.chatgpttelegrambot.domain.model.TelegramCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramCommandDispatcher {
    private final List<TelegramCommandHandler> telegramCommandHandlerList;

    public BotApiMethod<?> processCommand(Message message) {
        var text = message.getText();
        TelegramCommandHandler commandHandler = telegramCommandHandlerList.stream()
                .filter(handler -> handler.getSupportedCommand().getCommandName().equals(text))
                .findFirst()
                .orElseGet(() ->
                        telegramCommandHandlerList
                                .stream()
                                .filter(h -> h.getSupportedCommand().equals(TelegramCommand.NOT_A_COMMAND))
                                .findFirst()
                                .get());
        return commandHandler.processCommand(message);
    }

    public boolean isCommand(Message message) {
        return message.hasText() && message.getText().startsWith("/");
    }
}
