package com.ivko.hello;

import com.ivko.hello.manager.ManagementLayer;
import com.ivko.hello.model.Contact;
import org.h2.tools.DeleteDbFiles;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertEquals;

public class TestRegexMethodWithH2DataBase {
    private static final Logger LOG = Logger.getLogger(String.valueOf(TestRegexMethodWithH2DataBase.class));

    private static Connection dataBaseInit() {
        Connection connection = null;
        try {
            DeleteDbFiles.execute("~", "test", false);
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/test");
            Statement statement = connection.createStatement();
            statement.execute("create table contacts(id bigint primary key, name varchar(255))");
            statement.execute("insert into contacts values(1, 'John Connor')");
            statement.execute("insert into contacts values(2, 'Sarah Connor')");
            statement.execute("insert into contacts values(3, 'Bill Clinton')");
            statement.execute("insert into contacts values(4, 'Barak Obama')");
            statement.execute("insert into contacts values(5, 'Bill Gates')");
            statement.execute("insert into contacts values(6, 'Linus Torvalds')");
            statement.execute("insert into contacts values(7, 'Steeve Jobs')");
            statement.execute("insert into contacts values(8, 'Alice Cooper')");
            statement.execute("insert into contacts values(9, 'Kenny McCormick')");
            statement.execute("insert into contacts values(10, 'Eric Cartmann')");
        } catch (SQLException | ClassNotFoundException e) {
            LOG.info(e.getMessage());
        }
        return connection;
    }

    @Test
    public void testRegexMethodIfRegexIsCorrectAndNotNullAndNotEmpty() {
        //GIVEN:
        List<Contact> contacts;
        Connection connection = dataBaseInit();
        String regex;
        int expectedSize;
        try {
            //WHEN:
            regex = "^Z.*$";
            expectedSize = 10;
            contacts = ManagementLayer.getInstance().getFilteredContacts(regex);

            //THEN:
            assertEquals(expectedSize, contacts.size());

            //WHEN:
            regex = "^B.*$";
            expectedSize = 7;
            contacts = ManagementLayer.getInstance().getFilteredContacts(regex);

            //THEN:
            assertEquals(expectedSize, contacts.size());

            //WHEN:
            regex = "^.*[a-z].*$";
            expectedSize = 0;
            contacts = ManagementLayer.getInstance().getFilteredContacts(regex);

            //THEN:
            assertEquals(expectedSize, contacts.size());

            //WHEN:
            regex = "^.*[B].*$";
            expectedSize = 7;
            contacts = ManagementLayer.getInstance().getFilteredContacts(regex);

            //THEN:
            assertEquals(expectedSize, contacts.size());
        } catch (SQLException e) {
            LOG.info(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOG.info(e.getMessage());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testRegexMethodIfRegexIsNull() {
        //GIVEN:
        Connection connection = dataBaseInit();
        List<Contact> contacts;
        String regex;
        int expectedSize;
        try {
            //WHEN:
            regex = null;
            expectedSize = 0;
            contacts = ManagementLayer.getInstance().getFilteredContacts(regex);

            //THEN:
            assertEquals(expectedSize, contacts.size());
        } catch (SQLException e) {
            LOG.info(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOG.info(e.getMessage());
            }
        }
    }

    @Test(expected = PatternSyntaxException.class)
    public void testRegexMethodIfNameFilterIsIncorrectRegexOrNameFilterIsEmpty() {
        //GIVEN:
        Connection connection = dataBaseInit();
        List<Contact> contacts;
        String regex;
        try {
            //WHEN:
            regex = "kkk***===888888pp/tttttt";

            //THEN:
            contacts = ManagementLayer.getInstance().getFilteredContacts(regex);

            //WHEN:
            regex = "";

            //THEN:
            contacts = ManagementLayer.getInstance().getFilteredContacts(regex);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOG.info(e.getMessage());
            }
        }
    }
}