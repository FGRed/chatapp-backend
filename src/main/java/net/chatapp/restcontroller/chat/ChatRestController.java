package net.chatapp.restcontroller.chat;

import net.chatapp.model.chat.Chat;
import net.chatapp.service.chat.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatRestController {
    
    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/chats")
    ResponseEntity<List<Chat>> getChats(){
        List<Chat> chats = chatService.getChats();
        if(chats == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(chats);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/exists/{cids}")
    ResponseEntity<Chat> exists(@PathVariable("cids") final Long... cids){
        Chat exists = chatService.findChatsByParticipants(cids);
        return ResponseEntity.ok(exists);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/")
    ResponseEntity<Chat> createChat(@RequestParam("participants") final Long... pids){
        Chat chat = chatService.createChat(pids);
        return ResponseEntity.ok(chat);
    }


}