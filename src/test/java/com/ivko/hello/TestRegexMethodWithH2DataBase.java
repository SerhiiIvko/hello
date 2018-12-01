package com.ivko.hello;

import com.ivko.hello.manager.DbManager;
import com.ivko.hello.manager.NameFilter;
import com.ivko.hello.model.Contact;
import org.h2.tools.DeleteDbFiles;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestRegexMethodWithH2DataBase {
    private static final Logger LOG = Logger.getLogger(String.valueOf(TestRegexMethodWithH2DataBase.class));

    @BeforeClass
    public static void init() throws SQLException {
        org.h2.tools.Server.createTcpServer().start();
        DbManager.initialize(DbManager.getH2DataSource());
    }

    @Before
    public void resetDb() {
        Connection connection;
        try {
            DeleteDbFiles.execute("~", "test", true);
            connection = DbManager.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE if exists contacts");
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
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        }
    }

    @Test
    public void testRegexMethodIfRegexIsCorrectAndNotNullAndNotEmpty() {
        //GIVEN:
        NameFilter nameFilter = new NameFilter();
        List<Contact> contacts;
        String regex;
        int expectedSize;

        //WHEN:
        regex = "^Z.*$";
        expectedSize = 10;
        contacts = nameFilter.getFilteredContacts(regex);

        //THEN:
        assertEquals(expectedSize, contacts.size());

        //WHEN:
        regex = "^B.*$";
        expectedSize = 7;
        contacts = nameFilter.getFilteredContacts(regex);

        //THEN:
        assertEquals(expectedSize, contacts.size());

        //WHEN:
        regex = "^.*[a-z].*$";
        expectedSize = 0;
        contacts = nameFilter.getFilteredContacts(regex);

        //THEN:
        assertEquals(expectedSize, contacts.size());

        //WHEN:
        regex = "^.*[B].*$";
        expectedSize = 7;
        contacts = nameFilter.getFilteredContacts(regex);

        //THEN:
        assertEquals(expectedSize, contacts.size());
    }

    @Test
    public void testConcurrency() throws ExecutionException, InterruptedException {
        //GIVEN:
        NameFilter nameFilter = new NameFilter();
        String[] regexes = {"^Z.*$", "^B.*$", "^.*[a-z].*$", "^.*[B].*$"};
        int[] expectedSizes = {10, 7, 0, 7};
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Function<String, Callable<List<Contact>>> executedFunction = regex ->
                (Callable<List<Contact>>) () -> nameFilter.getFilteredContacts(regex);
        List<Callable<List<Contact>>> executedNameFilters = Stream.of(regexes)
                .map(executedFunction)
                .collect(Collectors.toList());

        //WHEN:
        List<Future<List<Contact>>> results = executedNameFilters.stream()
                .map(executor::submit)
                .collect(Collectors.toList());

        //THEN:
        for (int i = 0; i < results.size(); ++i) {
            Future<List<Contact>> result = results.get(i);
            List<Contact> contacts = result.get();
            assertEquals(expectedSizes[i], contacts.size());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testRegexMethodIfRegexIsNull() {
        //GIVEN:
        NameFilter nameFilter = new NameFilter();
        List<Contact> contacts;
        String regex;
        int expectedSize;

        //WHEN:
        regex = null;
        expectedSize = 0;
        contacts = nameFilter.getFilteredContacts(regex);

        //THEN:
        assertEquals(expectedSize, contacts.size());
    }

    @Test(expected = PatternSyntaxException.class)
    public void testRegexMethodIfNameFilterIsIncorrectRegexOrNameFilterIsEmpty() {
        //GIVEN:
        NameFilter nameFilter = new NameFilter();
        List<Contact> contacts;
        String regex;

        //WHEN:
        regex = "kkk***===888888pp/tttttt";

        //THEN:
        contacts = nameFilter.getFilteredContacts(regex);

        //WHEN:
        regex = "";

        //THEN:
        contacts = nameFilter.getFilteredContacts(regex);
    }
}