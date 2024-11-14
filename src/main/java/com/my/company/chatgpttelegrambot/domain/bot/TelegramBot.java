package com.my.company.chatgpttelegrambot.domain.bot;

import com.my.company.chatgpttelegrambot.api.openai.OpenAIClient;
import com.my.company.chatgpttelegrambot.api.openai.model.DataType;
import com.my.company.chatgpttelegrambot.api.openai.model.request.OpenAIRequest;
import com.my.company.chatgpttelegrambot.api.openai.model.response.Response;
import com.my.company.chatgpttelegrambot.api.openai.model.response.SimpleTextResponse;
import com.my.company.chatgpttelegrambot.domain.service.ChatGptModelStrategy;
import com.my.company.chatgpttelegrambot.domain.service.ChatGptService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private final ChatGptModelStrategy strategy;

    public TelegramBot(DefaultBotOptions options, String botToken, String botName, ChatGptModelStrategy strategy) {
        super(options, botToken);
        this.botName = botName;
        this.strategy = strategy;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            log.info("message update from bot: {} for chat with id: {}", text, chatId);

            if(text.equals("/start")){
                SimpleTextResponse response = new SimpleTextResponse("hello, let`s start!");
                sendResponse(chatId, response);
                return;
            }

            var openAIResponse = strategy.getOpenAIResponse(update.getMessage().getFrom().getId(), text, DataType.TEXT);
            log.info("received openApi response for user with id: {}", update.getMessage().getFrom().getId());
            sendResponse(chatId, openAIResponse);
        }
    }

    @SneakyThrows
    private void sendResponse(Long chatId, Response response) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), response.getContent());
        sendApiMethod(sendMessage);
    }

    @Override
    public String getBotUsername() {
        log.info("get bot name: {}", botName);
        return this.botName;
    }
}
