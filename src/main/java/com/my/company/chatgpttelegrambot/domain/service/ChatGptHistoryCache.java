package com.my.company.chatgpttelegrambot.domain.service;

import com.my.company.chatgpttelegrambot.api.openai.model.ChatHistory;
import com.my.company.chatgpttelegrambot.api.openai.model.Message;
import lombok.RequiredArgsConstructor;
import org.apache.el.util.ConcurrentCache;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatGptHistoryCache {
    private final ConcurrentCache<Long, ChatHistory> chatHistoryMap = new ConcurrentCache<>(100);

    public Optional<ChatHistory> getUserHistory(Long userId) {
        return Optional.ofNullable(chatHistoryMap.get(userId));
    }

    public void creatHistoryIfNotExist(Long userId) {
        if(getUserHistory(userId).isEmpty()){
            chatHistoryMap.put(userId, new ChatHistory(new ArrayList<>()));
        }
    }

    public ChatHistory addMessageToHistory(Long userId, Message message) {
        ChatHistory chatHistory = getUserHistory(userId)
                .orElseThrow(() -> new IllegalStateException("History does not exist for user =%s".formatted(userId)));
        List<Message> userList = chatHistory.chatMessages();
        if (userList.size() > 30) {
            userList.removeFirst();
        }
        userList.add(message);
        return chatHistory;
    }


}
