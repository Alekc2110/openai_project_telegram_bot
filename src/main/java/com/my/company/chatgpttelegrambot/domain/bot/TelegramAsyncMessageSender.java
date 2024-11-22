package com.my.company.chatgpttelegrambot.domain.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Component
public class TelegramAsyncMessageSender {
    private final DefaultAbsSender defaultAbsSender;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public TelegramAsyncMessageSender(@Lazy DefaultAbsSender defaultAbsSender) {
        this.defaultAbsSender = defaultAbsSender;
    }

    @SneakyThrows
    public void sendMessageAsync(Long chatId,
                                 Supplier<SendMessage> action,
                                 Function<Throwable, SendMessage> onErrorHandler) {
        var message = defaultAbsSender.execute(
                SendMessage.builder()
                        .chatId(chatId)
                        .text("Your request is processing, please wait...")
                        .build());

        CompletableFuture.supplyAsync(action, executorService)
                .exceptionally(onErrorHandler)
                .thenAccept(receivedMessage ->
                {
                    try {
                        defaultAbsSender.execute(EditMessageText.builder()
                                .chatId(chatId)
                                .messageId(message.getMessageId())
                                .text(receivedMessage.getText())
                                .build());
                    } catch (TelegramApiException e) {
                        log.error("Error while send request to telegram", e);
                        throw new RuntimeException("Error while send request to telegram", e);
                    }
                });
    }
}

