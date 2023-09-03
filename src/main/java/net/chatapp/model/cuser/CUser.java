package net.chatapp.model.cuser;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chatapp.model.contact.Contact;
import net.chatapp.model.deviceinformation.DeviceInformation;
import net.chatapp.validator.CUserValidation;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class CUser implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotNull
   private String username;

   @JsonBackReference("password")
   @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).{8,}$",
           message = "Password must have at least 8 characters, one uppercase letter, and one special character")
   private String password;

   @Email
   private String email;

   @Lob
   @Basic(fetch = FetchType.LAZY)
   @Type(type = "org.hibernate.type.ImageType")
   private byte[] avatar;
   @NotNull
   private String role;

   @JsonBackReference
   boolean accountExpired  = true,
           accountNonLocked = true,
           accountNonExpired  = true,
           credentialsNonExpired = true,
           enabled = true;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<GrantedAuthority> list = new ArrayList<>();
      list.add(new SimpleGrantedAuthority("ROLE_" + role));
      return list;
   }

   private String sessionId;

   private boolean online;

   private Date creationDate = new Date();
   private Date lastLogin = new Date();

   @Column(length = 100)
   private String freeWord;

   @OneToOne
   @JsonBackReference("device-information")
   private DeviceInformation deviceInformation;
}