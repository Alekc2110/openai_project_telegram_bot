package com.my.company.chatgpttelegrambot.domain.bot;

import com.my.company.chatgpttelegrambot.domain.bot.handler.datahandler.TelegramDataHandler;
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

import java.io.File;


@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private final ChatGptModelStrategy strategy;
    private final TelegramCommandDispatcher commandDispatcher;
    private final TelegramDataHandler<String, File> voiceHandler;

    public TelegramBot(String botToken, String botName, ChatGptModelStrategy strategy,
                       TelegramCommandDispatcher telegramCommandDispatcher, TelegramDataHandler<String, File> voiceHandler) {
        super(new DefaultBotOptions(), botToken);
        this.botName = botName;
        this.strategy = strategy;
        this.commandDispatcher = telegramCommandDispatcher;
        this.voiceHandler = voiceHandler;
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

            if(update.hasMessage() && update.getMessage().hasVoice()){
                processVoiceRequest(update);
            }
        } catch (TelegramApiException e) {
            log.error("Error while processing update: {}", e.getMessage());
            sendUserErrorMessage(update.getMessage().getChatId());
        }
    }

    private void processVoiceRequest(Update update) throws TelegramApiException {
        var chatId = update.getMessage().getChatId();
        var userId = update.getMessage().getFrom().getId();
        var fileId = update.getMessage().getVoice().getFileId();
        File file = voiceHandler.handleData(fileId);
        var openAIResponseOptional = strategy.getOpenAIResponse(userId, file, DataType.VOICE);
        if(openAIResponseOptional.isPresent()) {
            String transcribedUserText = openAIResponseOptional.get().getContent();
            var responseOptional = strategy.getOpenAIResponse(userId, transcribedUserText, DataType.TEXT);
            responseOptional.ifPresentOrElse(response -> sendUserResponse(chatId, response),
                    ()-> log.error("no text response received from chatGPT in processVoiceRequest method"));
        }
    }

    private void processTextRequest(Update update) throws TelegramApiException {
        var text = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        log.info("message update from bot: {} for chat with id: {}", text, chatId);
        var userId = update.getMessage().getFrom().getId();
        var openAITextResponseOptional = strategy.getOpenAIResponse(userId, text, DataType.TEXT);
        log.info("received openApi response for user with id: {}", update.getMessage().getFrom().getId());
        openAITextResponseOptional.ifPresentOrElse(response -> sendUserResponse(chatId, response),
                ()-> log.error("no text response received from chatGPT in processTextRequest method"));
    }

    @SneakyThrows
    private void sendUserResponse(Long chatId, Response response) {
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
