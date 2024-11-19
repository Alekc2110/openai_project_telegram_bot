package com.my.company.chatgpttelegrambot.domain.bot.handler.datahandler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;

import java.io.*;
import java.net.URI;
import java.net.URL;

@Slf4j
@Component
public class TelegramVoiceHandler implements TelegramDataHandler<String, java.io.File> {

    @Value("${bot.token}")
    private String botToken;
    private final DefaultAbsSender telegramSender;

    public TelegramVoiceHandler(@Lazy DefaultAbsSender telegramSender) {
        this.telegramSender = telegramSender;
    }

    @SneakyThrows
    @Override
    public java.io.File handleData(String fileId) {
        File telegramFile = telegramSender.execute(GetFile.builder()
                .fileId(fileId)
                .build());
        String fileUrl = telegramFile.getFileUrl(botToken);
        return getByteArrayFromUrl(fileUrl);
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
