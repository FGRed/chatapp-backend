package net.chatapp.repository.chat;

import net.chatapp.model.chat.Chat;

import java.util.List;
import java.util.Optional;

public interface CustomChatRepository {

    List<Chat> findChatsByUserId(Long uid);

}
