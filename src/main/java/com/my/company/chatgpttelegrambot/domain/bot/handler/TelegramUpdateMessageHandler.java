package com.my.company.chatgpttelegrambot.domain.bot.handler;

import com.my.company.chatgpttelegrambot.domain.bot.TelegramAsyncMessageSender;
import com.my.company.chatgpttelegrambot.domain.bot.TelegramCommandDispatcher;
import com.my.company.chatgpttelegrambot.domain.bot.handler.datahandler.TelegramDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Slf4j
@Component
public class TelegramUpdateMessageHandler {
    private final TelegramCommandDispatcher commandDispatcher;
    private final TelegramAsyncMessageSender asyncMessageSender;
    private final TelegramDataHandler<Message, SendMessage> textHandler;
    private final TelegramDataHandler<Message, SendMessage> voiceHandler;

    public TelegramUpdateMessageHandler(TelegramCommandDispatcher commandDispatcher,
                                        TelegramAsyncMessageSender asyncMessageSender,
                                        @Qualifier(value = "telegramTextHandler") TelegramDataHandler<Message, SendMessage> textHandler,
                                        @Qualifier(value = "telegramVoiceHandler") TelegramDataHandler<Message, SendMessage> voiceHandler) {
        this.commandDispatcher = commandDispatcher;
        this.asyncMessageSender = asyncMessageSender;
        this.textHandler = textHandler;
        this.voiceHandler = voiceHandler;
    }

    public Optional<BotApiMethod<?>> processMessageUpdate(Message message) {
        if (commandDispatcher.isCommand(message)) {
            BotApiMethod<?> apiMethod = commandDispatcher.processCommand(message);
            log.info("processed new command: {} ", message.getText());
            return Optional.of(apiMethod);
        }
        Long chatId = message.getChatId();
        if (message.hasVoice() || (message.hasText() && !message.getText().startsWith("/"))) {
            asyncMessageSender.sendMessageAsync(
                    chatId,
                    () -> handleMessageAsync(message),
                    error -> sendUserErrorMessage(error, chatId)
            );
        }
        return Optional.empty();
    }

    private SendMessage handleMessageAsync(Message message) {
        if (message.hasVoice()) {
            return voiceHandler.handleTelegramData(message);
        }
        return textHandler.handleTelegramData(message);
    }

    private SendMessage sendUserErrorMessage(Throwable error, Long chatId) {
        log.error("error happened when async process request ", error);
        return SendMessage.builder()
                .chatId(chatId)
                .text("Error happened, try later")
                .build();
    }
}
