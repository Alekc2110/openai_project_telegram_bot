package com.my.company.chatgpttelegrambot.api.bot.config;

import com.my.company.chatgpttelegrambot.api.bot.TelegramBot;
import com.my.company.chatgpttelegrambot.api.openai.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfiguration {

    @Value(value = "${bot.token}")
    private String botToken;
    @Value(value = "${bot.name}")
    private String botName;

    @Bean
    public TelegramBot telegramBot(TelegramBotsApi telegramBotsApi, OpenAIClient client) throws TelegramApiException {
        var bot = new TelegramBot(new DefaultBotOptions(), botToken, botName, client);
        telegramBotsApi.registerBot(bot);
        return bot;

    }
    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }
}
