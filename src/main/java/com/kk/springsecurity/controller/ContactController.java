package com.kk.springsecurity.controller;

import com.kk.springsecurity.model.Contact;
import com.kk.springsecurity.repository.ContactRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class ContactController {
    private final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @PostMapping("contact")
    /*
     it will filter out contact with contactName = 'Test',
     eg; if we have 5 contants in the list, and if there are 2 contacts with contactName == 'Test',
     then the contacts list will only have 3 contacts
     */
    /*
     in case of preFilter, only contacts with contactName other than 'Test' will be processed.
     in case of PostFilter, only contacts with contactName other than 'Test' will be send in the response.
     */
    // to apply PreFilter, the input must be of type collection interface eg; List<Contact>
    // to apply PostFilter, the output must be of type collection interface.
    // @PreFilter("filterObject.contactName != 'Test'")
    @PostFilter("filterObject.contactName != 'Test'")
    public List<Contact> saveContactInquiryDetails(@RequestBody List<Contact> contacts) {
        // need to handle empty contacts scenario otherwise we will get IndexOutOfBoundsException.
        Contact contact = contacts.get(0);
        contact.setContactId(getServiceReqNumber());
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        contact = contactRepository.save(contact);
        List<Contact> returnContacts = new ArrayList<>();
        returnContacts.add(contact);
        return returnContacts;
    }

    public String getServiceReqNumber() {
        Random random = new Random();
        int ranNum = random.nextInt(999999999 - 9999) + 9999;
        return "SR" + ranNum;
    }
}
