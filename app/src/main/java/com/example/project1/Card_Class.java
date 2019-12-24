package com.example.project1;

public class Card_Class {
    private String name;
    private String id;
    private String method;
    private String block;
    private String first_word;
    private String second_word;


    public Card_Class(String id, String name, String block, String method, String first_word, String second_word) {
        this.id = id;
        this.name = name;
        this.block = block;
        this.method = method;
        this.first_word = first_word;
        this.second_word = second_word;
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

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFirst_word() {
        return first_word;
    }

    public void setFirst_word(String first_word) {
        this.first_word = first_word;
    }

    public String getSecond_word() {
        return second_word;
    }

    public void setSecond_word(String second_word) {
        this.second_word = second_word;
    }
}

