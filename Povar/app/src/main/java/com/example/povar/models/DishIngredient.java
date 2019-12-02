package com.example.povar.models;

public class DishIngredient {
    private int id;
    private int dishid;
    private int ingredientid;
    private String quantity;
    private Ingredient ingredient;
    private boolean isAdded;

    public DishIngredient(int Id, int dishid, int ingredientid, String quantity, Ingredient i, boolean isAdded) {
        this.id = Id;
        this.dishid = dishid;
        this.ingredientid = ingredientid;
        this.quantity = quantity;
        this.ingredient = i;
        this.isAdded = isAdded;
    }

    public void SetDishId(int dishid) { this.dishid = dishid; }

    public int GetDishId() {
        return this.dishid;
    }

    public int GetId() {
        return this.id;
    }

    public void SetIngredientId(int ingredientid) {
        this.ingredientid = ingredientid;
    }

    public int GetIngredientId() {
        return this.ingredientid;
    }

    public void SetQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String GetQuantity() {
        return this.quantity;
    }

    public void SetIngredient(Ingredient i) {
        this.ingredient = i;
    }

    public Ingredient GetIngredient() {
        return this.ingredient;
    }

    public void SetIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public boolean GetIsAdded() {
        return this.isAdded;
    }
}
