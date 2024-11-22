package com.my.company.chatgpttelegrambot.domain.bot.handler.datahandler;

import com.my.company.chatgpttelegrambot.domain.model.DataType;
import com.my.company.chatgpttelegrambot.domain.model.response.Response;
import com.my.company.chatgpttelegrambot.domain.service.ChatGptModelStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

@Slf4j
@Component
public class TelegramVoiceHandler implements TelegramDataHandler<Message, SendMessage> {

    @Value("${bot.token}")
    private String botToken;
    private final DefaultAbsSender telegramSender;
    private final ChatGptModelStrategy strategy;

    public TelegramVoiceHandler(@Lazy DefaultAbsSender telegramSender, ChatGptModelStrategy strategy) {
        this.telegramSender = telegramSender;
        this.strategy = strategy;
    }

    @SneakyThrows
    @Override
    public SendMessage handleTelegramData(Message message) {
        var chatId = message.getChatId();
        var userId = message.getFrom().getId();
        String fileUrl = getTelegramFileUrl(message.getVoice().getFileId());
        java.io.File tempAudioFile = getByteArrayFromUrl(fileUrl);
        SendMessage sendMessage = null;
        var openAIResponseOptional = strategy.getOpenAIResponse(userId, tempAudioFile, DataType.VOICE);
        if (openAIResponseOptional.isPresent()) {
            Response response = openAIResponseOptional.get();
            var responseOptional = strategy.getOpenAIResponse(userId, response.getContent(), DataType.TEXT);
            if (responseOptional.isPresent()) {
                Response textResponse = responseOptional.get();
                sendMessage = new SendMessage(chatId.toString(), textResponse.getContent());
            } else {
                log.error("no text response received from chatGPT while handle text request");
            }
        } else {
            log.error("no text response received from chatGPT while handle voice request");
        }
        return sendMessage;
    }

    @SneakyThrows
    private String getTelegramFileUrl(String fileId) {
        File telegramFile = telegramSender.execute(GetFile.builder()
                .fileId(fileId)
                .build());
        return telegramFile.getFileUrl(botToken);
    }

    @SneakyThrows
    private java.io.File getByteArrayFromUrl(String fileUrl) {
        URL url = URI.create(fileUrl).toURL();
        var tempFile = java.io.File.createTempFile("telegram", ".ogg");

        try (InputStream inputStream = url.openStream();
             OutputStream outputStream = new FileOutputStream(tempFile)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("Error while downloading file: {}", e.getMessage());
            throw new RuntimeException("Error while downloading file", e);
        }
        return tempFile;
    }
}
