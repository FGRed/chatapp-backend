package net.chatapp.repository.chat;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.chat.Chat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID>, CustomChatRepository {

    @Query("select c from Chat c join c.chatParticipants as participants where ?1 in participants.id")
    List<Chat> findByChats(Long uid);


}