package com.ivko.hello.model;

import java.util.Objects;

public class Contact {
    private long id;
    private String name;

    public Contact() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return id == contact.id &&
                name.equals(contact.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}