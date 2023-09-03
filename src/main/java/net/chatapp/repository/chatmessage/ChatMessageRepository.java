package net.chatapp.repository.chatmessage;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.chatmessage.ChatMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query("select cm from ChatMessage cm where cm.chatUUID = :cUUID order by cm.date asc")
    List<ChatMessage> findByChatId(@Param("cUUID") UUID cUUID);

}