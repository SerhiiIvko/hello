package com.ivko.hello.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Contact {
    private int id;
    private String name;

    public Contact() {
    }

    public Contact(ResultSet resultSet) throws SQLException {
        setId(resultSet.getInt(1));
        setName(resultSet.getString(2));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contact [Id=" + id + ", Name=" + name + "]";
    }
}