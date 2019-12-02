package com.example.povar.DishActivityFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.povar.Adapters.DishIngredientsAdapter;
import com.example.povar.DBRepository;
import com.example.povar.R;
import com.example.povar.models.Dish;
import com.example.povar.models.DishIngredient;
import com.example.povar.models.Ingredient;

import java.util.List;

public class DishIngredientsFragment extends Fragment {
    DBRepository db;
    List<DishIngredient> ingredients;
    List<Ingredient> allingredients;
    int key;
    Dish dish;
    DishIngredientsAdapter adapter;
    RecyclerView rv_dish_ingredients;
    DishIngredientsAdapter.OnButtonClickListener listener;

    public DishIngredientsFragment() { }

    public static DishIngredientsFragment newInstance() {
        return new DishIngredientsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_ingredients_fragment, container, false);
        rv_dish_ingredients = (RecyclerView) view.findViewById(R.id.rv_dish_ingredients);
        GetBundle();
        SetData();
        adapter = SetIngredientsAdapter();
        rv_dish_ingredients.setAdapter(adapter);
        return view;
    }

    public void SetListener(DishIngredientsAdapter.OnButtonClickListener listener) {
        this.listener = listener;
    }

    public void SetIngredients(List<DishIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    private void SetData() {
        allingredients = db.getIngredients();
    }

    private DishIngredientsAdapter SetIngredientsAdapter() {
        return new DishIngredientsAdapter(key, getActivity(), listener,  ingredients, allingredients);
    }

    private void GetBundle() {
        Bundle args = this.getArguments();
        key = args.getInt("key");
        db = (DBRepository) args.getSerializable("db");
        dish = (Dish) args.getSerializable("dish");
    }
}
