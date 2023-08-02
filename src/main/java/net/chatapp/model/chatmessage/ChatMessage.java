package net.chatapp.model.chatmessage;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class ChatMessage{
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private Long id;

}