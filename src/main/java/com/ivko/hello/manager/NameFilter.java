package com.ivko.hello.manager;

import com.ivko.hello.model.Contact;
import com.ivko.hello.service.DbConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class NameFilter {
    private Map<Long, String> contacts;

    public List<Contact> getFilteredContacts(String regex) throws PatternSyntaxException {
        if (contacts == null) {
            DbConnector dbConnector = new DbConnector();
            contacts = dbConnector.getContacts();
        }
        return applyFilter(regex);
    }

    private List<Contact> applyFilter(String regex) {
        List<Contact> filteredContacts = new ArrayList<>();
        contacts.entrySet().stream().filter(i -> !i.getValue().matches(regex)).forEach(i -> {
            Contact contact = new Contact();
            contact.setId(i.getKey());
            contact.setName(i.getValue());
            filteredContacts.add(contact);
        });
        return filteredContacts;
    }
}