package com.zalbyeco.albertolaz.moodly.service.mock;

import com.zalbyeco.albertolaz.moodly.users.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertoLaz on 17/09/2016.
 */
public class ContactsListServiceMock {

    private static ContactsListServiceMock instance;

    private ContactsListServiceMock() {}

    public static ContactsListServiceMock getContactsListServiceMock() {

        if (instance == null) {
            instance = new ContactsListServiceMock();
        }

        return instance;
    }


    public List<Contact> retrieveContacts(String currentUserName) {

        ArrayList<Contact> contactsToReturn = new ArrayList<Contact>();

        Contact contact1 = Contact.initializeStandardContact("zalby88@hotmail.it");
        Contact contact2 = Contact.initializeStandardContact("uomoditest@mailditest.it");

        contactsToReturn.add(contact1);
        contactsToReturn.add(contact2);

        return contactsToReturn;

    }//retrieveContacts mocked method
}
