package com.ivko.hello.manager;

import com.ivko.hello.model.Contact;
import com.ivko.hello.service.CacheServiceLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

//Требует доработки:
//1. Отсутствует описание как развернуть проект
//2. Работа с базой вручную через громоздкий, не очень читабильный код
//3. Вычитывать все записи из базы на каждый запрос от каждого пользователя неэффективно
//4. Для невалиднго регулярнго выражение - некорректный статус ответа
//5. Использование system.out вместо логирования

public class ManagementLayer {

    private ManagementLayer() {
    }

    private static class ManagementHolder {
        static final ManagementLayer Instance = new ManagementLayer();
    }

    public static synchronized ManagementLayer getInstance() {
        return ManagementHolder.Instance;
    }

    public List<Contact> getFilteredContacts(String regex, Connection dbConnection)
            throws SQLException, PatternSyntaxException {
        DBManager dbManager = new DBManager();
        if (dbConnection == null) {
            dbConnection = dbManager.getDBConnection();
        }
        List<Contact> contacts = new ArrayList<>();
        ResultSet resultSet = null;
        Map<Long, String> dbCache = null;
        try {
            if (dbConnection != null) {
                resultSet = dbManager.createDBStatement(dbConnection);
                dbCache = CacheServiceLayer.createCacheFromDB(resultSet);
                applyFilter(regex, contacts, dbCache);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
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