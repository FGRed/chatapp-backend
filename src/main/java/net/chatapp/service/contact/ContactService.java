package net.chatapp.service.contact;

import net.chatapp.error.CUserNotFoundException;
import net.chatapp.error.ContactAlreadyAddedException;
import net.chatapp.error.ContactNotFoundException;
import net.chatapp.model.contact.Contact;
import net.chatapp.model.cuser.CUser;
import net.chatapp.repository.contact.ContactRepository;
import net.chatapp.service.cuser.CUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final CUserService cUserService;

    public ContactService(ContactRepository contactRepository, CUserService cUserService) {
        this.contactRepository = contactRepository;
        this.cUserService = cUserService;
    }

    public List<Contact> getUsersContacts() {
        CUser sessionUser = cUserService.getCurrentSessionUser();
        if (sessionUser == null) {
            throw new CUserNotFoundException("Can't fetch contacts of non-user!");
        }
        return contactRepository.getUsersContacts(sessionUser.getId());
    }

    @Transactional
    public void deleteContactById(final Long contactId) {
        if (contactId == null) {
            throw new NullPointerException("ContactId can't be null!");
        }
        try {
            contactRepository.deleteById(contactId);
        } catch (EmptyResultDataAccessException exception) {
            throw new ContactNotFoundException("No contacts were found with id " + contactId);
        }
    }

    /**
    * Adds contact to currentSessionUser
    *
    * @param contactUserId id of contact's user
    **/
    public Contact addContact(final Long contactUserId) {
        if (contactUserId == null) {
            throw new NullPointerException("Contact id can't be null!");
        }
        CUser contactUser = cUserService.findById(contactUserId);
        CUser currentSessionUser = cUserService.getCurrentSessionUser();
        if (currentSessionUser == null) {
            throw new CUserNotFoundException("User must be logged in to add contact!");
        }

        boolean contactAlreadyAdded = contactRepository.contactAlreadyAddedForUser(contactUserId, currentSessionUser.getId());
        if (contactAlreadyAdded) {
            throw new ContactAlreadyAddedException();
        }

        Contact contact = new Contact(contactUser, currentSessionUser);
        Contact contact2 = new Contact(currentSessionUser, contactUser);
        return contactRepository.save(contact);
    }

    public boolean exists(final Long userId) {
        Long sessionUserId = cUserService.getCurrentSessionUser().getId();
        return contactRepository.contactAlreadyAddedForUser(userId, sessionUserId);
    }

}