package com.ivko.hello.manager;

import com.ivko.hello.model.Contact;
import com.ivko.hello.service.CacheServiceLayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import static com.ivko.hello.service.CacheServiceLayer.createCacheFromDB;

public class ManagementLayer {

    private ManagementLayer() {
    }

    private static class ManagementHolder {
        static final ManagementLayer Instance = new ManagementLayer();
    }

    public static synchronized ManagementLayer getInstance() {
        return ManagementHolder.Instance;
    }

    public List<Contact> getFilteredContacts(String regex)
            throws SQLException, PatternSyntaxException {
        List<Contact> contacts = new ArrayList<>();
        if (CacheServiceLayer.isCacheInitialized()) {
            applyFilter(regex, contacts, createCacheFromDB());
        } else {
            Map<Long, String> dbCache = createCacheFromDB();
            applyFilter(regex, contacts, dbCache);
        }
        return contacts;
    }

    private void applyFilter(String regex, List<Contact> contacts, Map<Long, String> cache) {
        cache.entrySet().stream().filter(i -> !i.getValue().matches(regex)).forEach(i -> {
            Contact contact = new Contact();
            contact.setId(i.getKey());
            contact.setName(i.getValue());
            contacts.add(contact);
        });
    }
}