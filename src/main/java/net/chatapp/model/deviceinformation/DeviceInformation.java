package net.chatapp.model.deviceinformation;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class DeviceInformation{
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private UUID id;
   private String deviceName;
   private String ip;
   private String location;

}