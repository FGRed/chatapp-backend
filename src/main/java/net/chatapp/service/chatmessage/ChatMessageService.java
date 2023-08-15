package net.chatapp.service.chatmessage;

import net.chatapp.model.chatmessage.ChatMessage;
import net.chatapp.model.chatmessage.ChatMessageDTO;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.chatmessage.ChatMessageRepository;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private CUserService cUserService;


    public List<ChatMessage> getByChatUUID(UUID chatUUID){
       return chatMessageRepository.findByChatId(chatUUID);
    }

    public ChatMessage sendMessage(ChatMessageDTO chatMessageDTO){
        CUser receiver = cUserService.findById(chatMessageDTO.getReceiverId());
        CUser sender = cUserService.getCurrentSessionUser();
        if(receiver != null && sender != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(sender);
            chatMessage.setText(chatMessageDTO.getText());
            chatMessage.setReceiverId(receiver.getId());
            chatMessage.setChatUUID(chatMessageDTO.getChatId());
            return chatMessageRepository.save(chatMessage);
        }
        return null;
    }
}