package net.chatapp.model.contact;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.chatapp.model.cuser.CUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contact {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne
    @NotNull(message = "Contact user cannot be null!")
    private CUser contactUser;
    @NotNull(message = "Contact must have a user!")
    private Long relatesToUser;

    public Contact(final CUser contactUser, final CUser relatesToUser){
        this.contactUser = contactUser;
        this.relatesToUser = relatesToUser.getId();
    }


}