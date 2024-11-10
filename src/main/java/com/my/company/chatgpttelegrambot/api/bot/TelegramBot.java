package com.my.company.chatgpttelegrambot.api.bot;

import com.my.company.chatgpttelegrambot.api.openai.OpenAIClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private final OpenAIClient client;

    public TelegramBot(DefaultBotOptions options, String botToken, String botName, OpenAIClient client) {
        super(options, botToken);
        this.botName = botName;
        this.client = client;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            log.info("message update from bot: {} for chat with id: {}", text, chatId);

            var openAIResponse = client.createChatCompletion(text);
            log.info("received openApi response: {}",openAIResponse);

            SendMessage sendMessage = new SendMessage(chatId.toString(), openAIResponse.choices().get(0).message().content());
            sendApiMethod(sendMessage);
        }
    }

    @Override
    public String getBotUsername() {
        log.info("get bot name: {}", botName);
        return this.botName;
    }
}
