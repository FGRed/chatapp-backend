package net.chatapp.service.chatmessage;

import net.chatapp.model.chatmessage.ChatMessage;
import net.chatapp.model.chatmessage.ChatMessageDTO;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.chatmessage.ChatMessageRepository;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private CUserService cUserService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    public List<ChatMessage> fetchChatsMessages(UUID chatUUID) {
        List<ChatMessage> chatMessages =  chatMessageRepository.findByChatId(chatUUID);
        List<ChatMessage> justReadMessages = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessages){
            if(!chatMessage.isRead()){
                chatMessage.setRead(true);
                chatMessage = chatMessageRepository.save(chatMessage);
                justReadMessages.add(chatMessage);
            }
        }
        if(!justReadMessages.isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topics/unread", justReadMessages);
        }
        return chatMessages;
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