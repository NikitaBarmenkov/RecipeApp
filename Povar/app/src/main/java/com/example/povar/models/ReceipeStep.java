package com.example.povar.models;

public class ReceipeStep {
    private int id;
    private String receipe_step_text;
    private String receipe_step_number;
    private int dishId;
    private Dish dish;

    public ReceipeStep(String text, String number, Dish dish){
        this.receipe_step_text = text;
        this.receipe_step_number = number;
        this.dish = dish;
    }

    public int GetId() {
        return this.id;
    }

    public void SetText(String Text) {
        this.receipe_step_text = Text;
    }

    public String GetText() {
        return receipe_step_text;
    }

    public void SetNumber(String Number) {
        this.receipe_step_number = Number;
    }

    public String GetNumber() {
        return receipe_step_number;
    }

    public void SetDishId(int id) {
        this.dishId = id;
    }

    public int GetDishId() {
        return dishId;
    }
}
