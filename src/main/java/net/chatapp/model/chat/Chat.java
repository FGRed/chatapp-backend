package net.chatapp.model.chat;

import lombok.Getter;
import lombok.Setter;
import net.chatapp.model.cuser.CUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Chat {
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private UUID id;

   @OneToMany
   private List<CUser> chatParticipants;

   private Long chatCreatorId;

   private String lastMessage;

   public void addToParticipants(CUser user) {
      if (chatParticipants == null) {
         chatParticipants = new ArrayList<>();
      }

      chatParticipants.add(user);
   }
}