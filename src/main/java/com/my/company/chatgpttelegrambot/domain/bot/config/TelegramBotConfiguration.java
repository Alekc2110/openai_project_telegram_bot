package com.my.company.chatgpttelegrambot.domain.bot.config;

import com.my.company.chatgpttelegrambot.domain.bot.TelegramBot;
import com.my.company.chatgpttelegrambot.domain.bot.TelegramCommandDispatcher;
import com.my.company.chatgpttelegrambot.domain.service.ChatGptModelStrategy;
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
    public TelegramBot telegramBot(TelegramBotsApi telegramBotsApi, ChatGptModelStrategy strategy,
                                   TelegramCommandDispatcher telegramCommandDispatcher) throws TelegramApiException {
        var bot = new TelegramBot(new DefaultBotOptions(), botToken, botName, strategy, telegramCommandDispatcher);
        telegramBotsApi.registerBot(bot);
        return bot;

    }
    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }
}
