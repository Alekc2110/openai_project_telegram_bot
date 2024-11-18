package com.my.company.chatgpttelegrambot.domain.bot;

import com.my.company.chatgpttelegrambot.domain.bot.handler.TelegramCommandHandler;
import com.my.company.chatgpttelegrambot.domain.model.TelegramCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramCommandDispatcher {
    private final List<TelegramCommandHandler> telegramCommandHandlerList;

    public BotApiMethod<?> processCommand(Update update) {
        var text = update.getMessage().getText();
        TelegramCommandHandler commandHandler = telegramCommandHandlerList.stream()
                .filter(handler -> handler.getSupportedCommand().getCommandName().equals(text))
                .findFirst()
                .orElseGet(() ->
                        telegramCommandHandlerList
                                .stream()
                                .filter(h -> h.getSupportedCommand().equals(TelegramCommand.NOT_A_COMMAND))
                                .findFirst()
                                .get());
        return commandHandler.processCommand(update);
    }

    public boolean isCommand(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().startsWith("/");
    }
}
