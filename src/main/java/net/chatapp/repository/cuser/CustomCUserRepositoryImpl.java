package net.chatapp.repository.cuser;

import net.chatapp.model.cuser.CUser;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomCUserRepositoryImpl implements CustomCUserRepository{

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private CUserService cUserService;

    @Override
    public List<CUser> find(String keyword) {

        Long sessionUserID = cUserService.getCurrentSessionUser().getId();

        TypedQuery<CUser> query = entityManager.createQuery("select user from CUser user " +
                "where lower(user.username) like :keyword and user.id <> :sessionUserId", CUser.class);
        query.setParameter("keyword", "%"+keyword.toLowerCase()+"%");
        query.setParameter("sessionUserId", sessionUserID);
        return query.getResultList();
    }
}
