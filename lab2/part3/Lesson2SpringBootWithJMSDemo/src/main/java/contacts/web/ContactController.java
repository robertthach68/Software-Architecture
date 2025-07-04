package contacts.web;

import contacts.domain.Contact;
import contacts.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
public class ContactController {
    @Autowired
    private ContactService contactService;


    @GetMapping("/contacts/{firstName}")
    public ResponseEntity<?> getContact(@PathVariable String firstName) {
        Contact contact = contactService.findByFirstName(firstName);
        if (contact == null) {
            return new ResponseEntity<CustomErrorType>(new CustomErrorType("Contact with firstname= "
                    + firstName + " is not available"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Contact>(contact, HttpStatus.OK);
    }

    @PostMapping("/contacts")
    public ResponseEntity<?> addContact(@RequestBody Contact contact) {
        contactService.add(contact);
        return new ResponseEntity<Contact>(contact, HttpStatus.OK);
    }

    @DeleteMapping("/contacts/{firstName}")
    public ResponseEntity<?> deleteContact(@PathVariable String firstName) {
        Contact contact = contactService.findByFirstName(firstName);
        if (contact == null) {
            return new ResponseEntity<CustomErrorType>(new CustomErrorType("Contact with firstname= " + firstName + " is not available"),HttpStatus.NOT_FOUND);
        }
        contactService.delete(firstName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/contacts/{firstName}")
    public ResponseEntity<?> updateContact(@PathVariable String firstName, @RequestBody Contact contact) {
        contactService.update(contact);
        return new ResponseEntity<Contact>(contact, HttpStatus.OK);
    }

    @GetMapping("/contacts")
    public ResponseEntity<?> getAllContacts() {
        Contacts allcontacs = new Contacts(contactService.findAll());
        return new ResponseEntity<Contacts>(allcontacs, HttpStatus.OK);
    }
}
