package com.my.company.chatgpttelegrambot.domain.bot.handler.datahandler;

import com.my.company.chatgpttelegrambot.domain.model.DataType;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import com.my.company.chatgpttelegrambot.domain.service.ChatGptModelStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramTextHandler implements TelegramDataHandler<Message, BotApiMethod<?>> {

    private final ChatGptModelStrategy strategy;
    @Override
    public BotApiMethod<?> handleTelegramData(Message message) {
        var text = message.getText();
        var chatId = message.getChatId();
        log.info("message update from bot: {} for chat with id: {}", text, chatId);
        var userId = message.getFrom().getId();
        var openAITextResponseOptional = strategy.getOpenAIResponse(userId, text, DataType.TEXT);
        log.info("received openApi response for user with id: {}", message.getFrom().getId());
        SendMessage sendMessage = null;
        if (openAITextResponseOptional.isPresent()) {
            Response response = openAITextResponseOptional.get();
           sendMessage = new SendMessage(chatId.toString(), response.getContent());
        } else {
            log.error("no text response received from chatGPT while handle text request");
        }
        return sendMessage;
    }
}
