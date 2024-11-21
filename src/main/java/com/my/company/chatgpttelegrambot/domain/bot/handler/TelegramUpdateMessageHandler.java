package com.my.company.chatgpttelegrambot.domain.bot.handler;

import com.my.company.chatgpttelegrambot.domain.bot.TelegramCommandDispatcher;
import com.my.company.chatgpttelegrambot.domain.bot.handler.datahandler.TelegramDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Slf4j
@Component
public class TelegramUpdateMessageHandler {
    private final TelegramCommandDispatcher commandDispatcher;
    private final TelegramDataHandler<Message, BotApiMethod<?>> textHandler;
    private final TelegramDataHandler<Message, BotApiMethod<?>> voiceHandler;

    public TelegramUpdateMessageHandler(TelegramCommandDispatcher commandDispatcher,
                                        @Qualifier(value = "telegramTextHandler") TelegramDataHandler<Message, BotApiMethod<?>> textHandler,
                                        @Qualifier(value = "telegramVoiceHandler") TelegramDataHandler<Message, BotApiMethod<?>> voiceHandler) {
        this.commandDispatcher = commandDispatcher;
        this.textHandler = textHandler;
        this.voiceHandler = voiceHandler;
    }

    public Optional<BotApiMethod<?>> processMessageUpdate(Message message){
        if (commandDispatcher.isCommand(message)) {
            BotApiMethod<?> apiMethod = commandDispatcher.processCommand(message);
            log.info("processed new command: {} ", message.getText());
            return Optional.of(apiMethod);
        }

        if (message.hasText() && !message.getText().startsWith("/")) {
           return Optional.ofNullable(textHandler.handleTelegramData(message));
        }

        if(message.hasVoice()){
            return Optional.ofNullable(voiceHandler.handleTelegramData(message));
        }
        return Optional.empty();
    }
}
