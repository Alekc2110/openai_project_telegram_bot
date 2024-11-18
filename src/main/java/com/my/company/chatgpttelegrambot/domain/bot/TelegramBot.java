package com.my.company.chatgpttelegrambot.domain.bot;

import com.my.company.chatgpttelegrambot.domain.model.DataType;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import com.my.company.chatgpttelegrambot.domain.model.response.SimpleTextResponse;
import com.my.company.chatgpttelegrambot.domain.service.ChatGptModelStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private final ChatGptModelStrategy strategy;
    private final TelegramCommandDispatcher commandDispatcher;

    public TelegramBot(DefaultBotOptions options, String botToken, String botName, ChatGptModelStrategy strategy, TelegramCommandDispatcher telegramCommandDispatcher) {
        super(options, botToken);
        this.botName = botName;
        this.strategy = strategy;
        this.commandDispatcher = telegramCommandDispatcher;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (commandDispatcher.isCommand(update)) {
                BotApiMethod<?> apiMethod = commandDispatcher.processCommand(update);
                log.info("processed new command: {} ", update.getMessage().getText());
                sendApiMethod(apiMethod);
            }

            if (update.hasMessage() && update.getMessage().hasText() && !update.getMessage().getText().startsWith("/")) {
                processTextRequest(update);
            }
        } catch (TelegramApiException e) {
            log.error("Error while processing update: {}", e.getMessage());
            sendUserErrorMessage(update.getMessage().getChatId());
        }
    }

    private void processTextRequest(Update update) throws TelegramApiException {
        var text = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        log.info("message update from bot: {} for chat with id: {}", text, chatId);
        var userId = update.getMessage().getFrom().getId();
        var openAITextResponse = strategy.getOpenAIResponse(userId, text, DataType.TEXT);
        log.info("received openApi response for user with id: {}", update.getMessage().getFrom().getId());
        sendUserResponse(chatId, openAITextResponse);
    }

    private void sendUserResponse(Long chatId, Response response) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId.toString(), response.getContent());
        sendApiMethod(sendMessage);
    }

    @SneakyThrows
    private void sendUserErrorMessage(Long userId){
        sendUserResponse(userId, new SimpleTextResponse("Error happen, try later"));
    }
    @Override
    public String getBotUsername() {
        log.info("get bot name: {}", botName);
        return this.botName;
    }
}
