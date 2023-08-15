package net.chatapp.model.chatmessage;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatMessageDTO {
    private String text;
    private Long receiverId;
    private UUID chatId;
}