package net.chatapp.model.chat;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class Chat{
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private Long id;

}