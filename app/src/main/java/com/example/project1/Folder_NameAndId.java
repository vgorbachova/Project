package com.example.project1;

public class Folder_NameAndId {
    private String name;
    private String id;

    public Folder_NameAndId(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
