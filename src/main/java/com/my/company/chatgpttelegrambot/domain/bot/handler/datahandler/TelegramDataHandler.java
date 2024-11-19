package com.my.company.chatgpttelegrambot.domain.bot.handler.datahandler;

public interface TelegramDataHandler<T, R> {

   R handleData(T input);
}
