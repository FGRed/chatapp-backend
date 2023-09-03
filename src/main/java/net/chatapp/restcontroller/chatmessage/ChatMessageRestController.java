package net.chatapp.restcontroller.chatmessage;

import net.chatapp.model.chatmessage.ChatMessage;
import net.chatapp.model.chatmessage.ChatMessageDTO;
import net.chatapp.service.chatmessage.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chatmessage")
public class ChatMessageRestController {

    private final ChatMessageService chatMessageService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatMessageRestController(ChatMessageService chatMessageService, SimpMessagingTemplate simpMessagingTemplate) {
        this.chatMessageService = chatMessageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/by-chat-uuid/{chatUUID}")
    ResponseEntity<List<ChatMessage>> getMessagesByChatID(@PathVariable("chatUUID") final UUID chatUUID){
        return ResponseEntity.ok(chatMessageService.fetchChatsMessages(chatUUID));
    }
    @PostMapping("/")
    ResponseEntity<List<ChatMessage>> postMessage(@RequestBody ChatMessageDTO chatMessageDTO){
        List<ChatMessage> chatMessages = chatMessageService.sendMessage(chatMessageDTO);
        if(!chatMessages.isEmpty()) {
            simpMessagingTemplate.convertAndSend("/topic/message", chatMessages);
            return ResponseEntity.ok(chatMessages);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @MessageMapping("/chat-messaging/")
    @SendTo("/topic/message")
    ChatMessage returnChat(@Payload ChatMessage chat) {
        return chat;
    }

    @PutMapping("/set-read")
    ResponseEntity<List<ChatMessage>> setRead(@RequestBody List<UUID> chatMessageIds) {
        List<ChatMessage> chatMessages = chatMessageService.setRead(chatMessageIds);
        simpMessagingTemplate.convertAndSend("/topic/unread", chatMessages);
        return ResponseEntity.ok().body(chatMessages);
    }

}