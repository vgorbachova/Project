package com.example.project1;

public class Block_Class {
    private String name;
    private String id;
    private String method;
    private String kind_of_block;
    private String folder;



    public Block_Class(String id, String name, String folder, String method, String kind_of_block) {
        this.id = id;
        this.name = name;
        this.folder = folder;
        this.method = method;
        this.kind_of_block = kind_of_block;
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
    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setKind(String kind_of_block) {
        this.kind_of_block = kind_of_block;
    }
    public String getKind() {
        return kind_of_block;
    }

}
