package net.chatapp.repository.chatmessage;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.chatmessage.ChatMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}