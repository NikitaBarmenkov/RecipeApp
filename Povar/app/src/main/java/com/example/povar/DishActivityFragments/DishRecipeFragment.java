package com.example.povar.DishActivityFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.povar.DBRepository;
import com.example.povar.R;
import com.example.povar.models.Dish;

public class DishRecipeFragment extends Fragment {
    EditText dishpage_recipe_text;
    private int key;
    DBRepository db;
    Dish dish;
    TextListener listener;

    public interface TextListener {
        public void OnRecipeTextChanged(String text);
    }

    public DishRecipeFragment() { }

    public static DishRecipeFragment newInstance() {
        return new DishRecipeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_recipes_fragment, container, false);
        dishpage_recipe_text = (EditText) view.findViewById(R.id.dishpage_recipe_text);
        GetBundle();
        switch(key) {
            case 0:
                dishpage_recipe_text.setEnabled(false);
                break;
            case 1:
            case 2:
                dishpage_recipe_text.setEnabled(true);
                dishpage_recipe_text.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        listener.OnRecipeTextChanged(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) { }
                });
                break;
        }
        dishpage_recipe_text.setText(dish.GetRecipe());
        return view;
    }

    private void GetBundle() {
        Bundle args = this.getArguments();
        key = args.getInt("key");
        db = (DBRepository) args.getSerializable("db");
        dish = (Dish) args.getSerializable("dish");
    }

    public void SetTextListener(TextListener listener) {
        this.listener = listener;
    }
}
