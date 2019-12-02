package com.example.povar.models;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Dish> dishes;

    public Category(int Id, String Name, List<Dish> Dishes) {
        this.id = Id;
        this.name = Name;
        this.dishes = Dishes;
    }

    public int GetId() {
        return id;
    }

    public void SetName(String Name) {
        this.name = Name;
    }

    public String GetName() {
        return this.name;
    }

    public void SetDishes(List<Dish> Dishes) {
        this.dishes = new ArrayList<>();
        this.dishes = Dishes;
    }

    public List<Dish> GetDishes() {
        return this.dishes;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
