package net.chatapp.service.chat;
import net.chatapp.model.chat.Chat;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.chat.ChatRepository;
import net.chatapp.service.BasicService;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService implements BasicService<Chat, UUID> {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private CUserService cUserService;
    public List<Chat> getChats(){
        final CUser cuser = cUserService.getCurrentSessionUser();
        if(cuser != null){
            return chatRepository.findChatsByUserId(cuser.getId());
        }
        return null;
    }

    @Override
    public Chat saveNew(Chat entity) {
        return chatRepository.save(entity);
    }

    @Override
    public Chat update(Chat entity) {
        throw new UnsupportedOperationException("This operation won't be supported for this service. User saveNew to update the entity");
    }

    @Override
    public Chat findById(UUID uuid) {
        return null;
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void delete(Chat entity) {

    }

    @Override
    public void deleteAll(Collection<Chat> entities) {

    }

    @Override
    public Collection<Chat> saveAll(Collection<Chat> entities) {
        return null;
    }
}