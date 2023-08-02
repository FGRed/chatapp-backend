package net.chatapp.model.settings;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class Settings{
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private Long id;

}