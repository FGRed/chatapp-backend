package net.chatapp.repository.contact;
import org.springframework.data.jpa.repository.JpaRepository;
import net.chatapp.model.contact.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("select contact from Contact contact where contact.relatesToUser = ?1")
    List<Contact> getUsersContacts(final Long uid);

    @Query("select count(c) > 0 from Contact c where c.contactUser.id = ?1 and c.relatesToUser = ?2")
    boolean contactAlreadyAddedForUser(final Long contactUser, final Long relatesTo);

}