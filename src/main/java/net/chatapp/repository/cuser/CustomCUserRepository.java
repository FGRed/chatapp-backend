package net.chatapp.repository.cuser;

import net.chatapp.model.cuser.CUser;

import java.util.List;

public interface CustomCUserRepository {

    List<CUser> find(String keyword);

}
