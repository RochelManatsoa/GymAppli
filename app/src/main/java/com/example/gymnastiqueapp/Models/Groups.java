package com.example.gymnastiqueapp.Models;

public class Groups {
    public String name, description, user, date;

    public Groups(String name, String description, String user, String date) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.date = date;
    }

    public Groups() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }
}
