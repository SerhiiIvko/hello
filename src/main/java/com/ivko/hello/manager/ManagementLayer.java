package com.ivko.hello.manager;

import com.ivko.hello.model.Contact;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ManagementLayer {
    private static final Logger LOG = Logger.getLogger(String.valueOf(ManagementLayer.class));

    private ManagementLayer() {
    }

    private static class ManagementHolder{
        static final ManagementLayer Instance = new ManagementLayer();
    }

    public static synchronized ManagementLayer getInstance() {
        return ManagementHolder.Instance;
    }

    public List<Contact> getFilteredContacts(String regex, Connection dbConnection)
            throws SQLException, PatternSyntaxException {
        Pattern pattern = Pattern.compile(regex);
        List<Contact> contacts = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        System.out.println("Method started");
        if (dbConnection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Context context = new InitialContext();
                DataSource dataSource = (DataSource) context.lookup("java:/jdbc/hello");
                dbConnection = dataSource.getConnection();
                System.out.println("Connection enable");
            } catch (NamingException|ClassNotFoundException e) {
                LOG.info(e.getMessage());
            }
        }
        try {
            if (dbConnection != null) {
                statement = dbConnection.createStatement();
                System.out.println("statement created");
                statement.setFetchSize(1);
                resultSet = statement.executeQuery("SELECT * FROM contacts");
                while (resultSet.next()) {
                    String contactNameString = resultSet.getString(2);
                    Matcher matcher = pattern.matcher(contactNameString);
                    if (!matcher.matches()) {
                        Contact contact = new Contact();
                        contact.setId(resultSet.getInt(1));
                        contact.setName(contactNameString);
                        contacts.add(contact);
                        System.out.println("contact added");
                    }
                }
            }
            System.out.println(contacts.toString());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        System.out.println(contacts);
        return contacts;
    }
}