package com.example.povar.models;

public class Ingredient {
    private int id;
    private String name;

    public Ingredient(String Name) {
        this.name = Name;
    }

    public Ingredient(int Id, String Name) {
        this.id = Id;
        this.name = Name;
    }

    public int GetId() {
        return this.id;
    }

    public void SetName(String Name) {
        this.name = Name;
    }

    public String GetName () {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
