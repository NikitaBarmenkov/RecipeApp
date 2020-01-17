package com.example.povar.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dish implements Serializable {
    private int id;
    @NonNull private String name;
    private byte[] pic;
    private double rating;
    private double kkal;
    private String recipe;
    private int category_id;
    private List<ReceipeStep> receipe_step_list;
    private List<Ingredient> ingredients;

    public Dish(){}

    public Dish(String name) {
        this.name = name;
    }

    public Dish(int id, @NonNull String name, double rating, byte[] image, double kkal, String recipe, int category_id){
        this.id = id;
        this.name=name;
        this.rating = rating;
        this.pic = image;
        this.kkal = kkal;
        this.recipe = recipe;
        this.category_id = category_id;
    }

    public int GetId() {
        return this.id;
    }

    public void SetName(String Name) {
        Name = setFirstLetter(Name);
        this.name = Name;
    }

    public String setFirstLetter(String s) {
        s = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
        return s;
    }

    public String GetName() {
        return name;
    }

    public void SetImage(byte[] Image) {
        this.pic = Image;
    }

    public byte[] GetImage() { return this.pic; }

    public void SetKkal(double Kkal) {
        this.kkal = Kkal;
    }

    public double GetKkal() { return this.kkal; }

    public void SetRating(double Rating) {
        this.rating = Rating;
    }

    public double GetRating() { return this.rating; }

    public void SetCategory(int Category) {
        this.category_id = Category;
    }

    public int GetCategory() { return this.category_id; }

    public void SetRecipe(String recipe) {
        //recipe = setStepText(recipe);
        this.recipe = recipe;
    }

    public String setStepText(String s) {
        if (s.matches("(.*)Шаг(.*)") || s.matches("Шаг(.*)")) {

        }
        return "";
    }

    public String GetRecipe() {
        return this.recipe;
    }

    public void SetReceipeSteps(List<ReceipeStep> receipes) {
        this.receipe_step_list = new ArrayList<>();
        this.receipe_step_list = receipes;
    }

    public List<ReceipeStep> GetReceipeSteps() {
        return this.receipe_step_list;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
