package com.my.company.chatgpttelegrambot.domain.bot;

import com.my.company.chatgpttelegrambot.domain.bot.handler.TelegramUpdateMessageHandler;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import com.my.company.chatgpttelegrambot.domain.model.response.SimpleTextResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value(value = "${bot.name}")
    private String botName;
    private final TelegramUpdateMessageHandler messageHandler;

    public TelegramBot(@Value(value = "${bot.token}") String botToken,
                       TelegramUpdateMessageHandler messageHandler) {
        super(new DefaultBotOptions(), botToken);
        this.messageHandler = messageHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Optional<BotApiMethod<?>> apiMethodOptional = messageHandler
                    .processMessageUpdate(update.getMessage());

            apiMethodOptional.ifPresent(apiMethod ->
                sendTelegramMessageAnswer(apiMethod, update.getMessage().getChatId()));
        }
    }

    private void sendTelegramMessageAnswer(BotApiMethod<?> apiMethod, Long chatId) {
        try {
            sendApiMethod(apiMethod);
        } catch (TelegramApiException e) {
            log.error("Error while processing message update: {}", e.getMessage());
            sendUserErrorMessage(chatId);
        }
    }
    @SneakyThrows
    private void sendUserResponse(Long chatId, Response response) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), response.getContent());
        sendApiMethod(sendMessage);
    }
    private void sendUserErrorMessage(Long chatId) {
        sendUserResponse(chatId, new SimpleTextResponse("Error happen, try later"));
    }
    @Override
    public String getBotUsername() {
        log.info("get bot name: {}", botName);
        return this.botName;
    }
}
