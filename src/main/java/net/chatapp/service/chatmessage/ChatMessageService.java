package net.chatapp.service.chatmessage;

import net.chatapp.model.chat.Chat;
import net.chatapp.model.chatmessage.ChatMessage;
import net.chatapp.model.chatmessage.ChatMessageDTO;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.chat.ChatRepository;
import net.chatapp.repository.chatmessage.ChatMessageRepository;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private ChatRepository chatRepository;


    public List<ChatMessage> fetchChatsMessages(UUID chatUUID) {
       return chatMessageRepository.findByChatId(chatUUID);
    }

    public List<ChatMessage> sendMessage(ChatMessageDTO chatMessageDTO){
        CUser receiver = cUserService.findById(chatMessageDTO.getReceiverId());
        CUser sender = cUserService.getCurrentSessionUser();
        if(receiver != null && sender != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(sender);
            chatMessage.setText(chatMessageDTO.getText());
            chatMessage.setReceiverId(receiver.getId());
            chatMessage.setChatUUID(chatMessageDTO.getChatId());
            chatMessageRepository.save(chatMessage);
            Chat chat = chatRepository.findById(chatMessage.getChatUUID()).get();
            chat.setLastMessage(chatMessage.getText());
            chatRepository.save(chat);
        }
        return chatMessageRepository.findByChatId(chatMessageDTO.getChatId());
    }

    public List<ChatMessage> setRead(List<Long> chatMessageIds){
        List<ChatMessage> chatMessages = chatMessageRepository.findAllById(chatMessageIds);
        for(ChatMessage chatMessage : chatMessages){
            chatMessage.setRead(true);
            chatMessage.setReadDate(new Date());
            chatMessage = chatMessageRepository.save(chatMessage);
        }
        return chatMessageRepository.findByChatId(chatMessages.get(0).getChatUUID());
    }

}