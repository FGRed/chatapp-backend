package net.chatapp.restcontroller.chatmessage;

import net.chatapp.model.chatmessage.ChatMessage;
import net.chatapp.model.chatmessage.ChatMessageDTO;
import net.chatapp.service.chatmessage.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/by-chat-uuid/{chatUUID}")
    ResponseEntity<List<ChatMessage>> getMessagesByChatID(@PathVariable("chatUUID") final UUID chatUUID){
        return ResponseEntity.ok(chatMessageService.getByChatUUID(chatUUID));
    }
    @PostMapping("/")
    ResponseEntity<ChatMessage> postMessage(@RequestBody ChatMessageDTO chatMessageDTO){
        ChatMessage cm = chatMessageService.sendMessage(chatMessageDTO);
        if(cm != null) {
            simpMessagingTemplate.convertAndSend("/topic/message", cm);
            return ResponseEntity.ok(cm);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @MessageMapping("/chat-messaging/")
    @SendTo("/topic/message")
    ChatMessage returnChat(@Payload ChatMessage chat) {
        return chat;
    }

}