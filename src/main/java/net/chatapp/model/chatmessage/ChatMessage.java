package net.chatapp.model.chatmessage;

import lombok.Getter;
import lombok.Setter;
import net.chatapp.model.cuser.CUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ChatMessage{
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private UUID id;
   @NotBlank(message = "Message text can't be null or empty.")
   private String text;
   private Date date;
   @NotNull(message = "Message must have a receiver.")
   private Long receiverId;
   @NotNull(message = "Message must have a sender.")
   @ManyToOne
   private CUser sender;
   @NotNull(message = "Message must be part of a chat. chatUUID can't be null.")
   private UUID chatUUID;

   @PrePersist
   private void prePersis(){
      this.date = new Date();
   }
   private boolean read;


}