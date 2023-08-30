package net.chatapp.repository.chat;

import net.chatapp.model.chat.Chat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomChatRepositoryImpl implements CustomChatRepository{

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public List<Chat> findChatsByUserId(Long uid) {
        TypedQuery<Chat> typedQuery = entityManager.createQuery(
                "select c from Chat c join c.chatParticipants as ppt where :uid in (ppt.id)", Chat.class)
                .setParameter("uid", uid);
        return typedQuery.getResultList();
    }
}
