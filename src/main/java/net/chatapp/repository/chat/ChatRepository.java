package net.chatapp.repository.chat;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.chat.Chat;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}