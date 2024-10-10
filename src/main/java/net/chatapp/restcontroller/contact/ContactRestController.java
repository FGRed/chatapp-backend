package net.chatapp.restcontroller.contact;

import net.chatapp.model.contact.Contact;
import net.chatapp.service.contact.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactRestController {

    private final ContactService contactService;

    public ContactRestController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping( "/")
    ResponseEntity<List<Contact>> getUsersContacts() {
        return ResponseEntity.ok(contactService.getUsersContacts());
    }

    @DeleteMapping( "/")
    ResponseEntity<Void> deleteContact(@RequestParam("contact-id") Long contactId) {
        contactService.deleteContactById(contactId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{contact-id}")
    ResponseEntity<Contact> addContact(@PathVariable("contact-id") Long contactId) {
        Contact contact = contactService.addContact(contactId);
        if(contact == null){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(contact);
    }

    @GetMapping("/exists/{userId}")
    ResponseEntity<Boolean> exists(@PathVariable("userId") final Long userId){
        return ResponseEntity.ok(contactService.exists(userId));
    }

}
