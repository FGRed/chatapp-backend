package net.chatapp.model.cuser;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class CUser implements UserDetails {
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private Long id;

   @NotNull
   private String username;

   @JsonBackReference
   private String password;

   private String email;
   private String avatar;
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

   private boolean online;

   private Date creationDate = new Date();
   private Date lastLogin = new Date();

}