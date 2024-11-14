package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.api.openai.model.ChatHistory;
import com.my.company.chatgpttelegrambot.api.openai.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatGptHistoryCache {
    private final Map<Long, ChatHistory> chatHistoryMap = new ConcurrentHashMap<>();

    public Optional<ChatHistory> getUserHistory(Long userId) {
        return Optional.ofNullable(chatHistoryMap.get(userId));
    }

    public void creatHistoryIfNotExist(Long userId) {
        if (!chatHistoryMap.containsKey(userId)) {
            chatHistoryMap.put(userId, new ChatHistory(new ArrayList<>()));
        }
    }

    public ChatHistory addMessageToHistory(Long userId, Message message) {
        ChatHistory chatHistory = getUserHistory(userId)
                .orElseThrow(() -> new IllegalStateException("History does not exist for user =%s".formatted(userId)));
        chatHistory.chatMessages().add(message);
        return chatHistory;
    }


}
