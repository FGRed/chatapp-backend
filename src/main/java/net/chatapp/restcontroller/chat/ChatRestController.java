package net.chatapp.restcontroller.chat;

import net.chatapp.model.chat.Chat;
import net.chatapp.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatRestController {
    
    @Autowired
    private ChatService chatService;

    @GetMapping("/chats")
    ResponseEntity<List<Chat>> getChats(){
        Long beginTime = System.currentTimeMillis();
        List<Chat> chats = chatService.getChats();
        if(chats == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("Whole request process took: " + (endTime - beginTime) + "ms.");
        return ResponseEntity.ok(chats);
    }


}