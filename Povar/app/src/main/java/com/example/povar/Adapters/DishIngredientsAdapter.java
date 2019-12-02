package com.example.povar.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povar.R;
import com.example.povar.models.DishIngredient;
import com.example.povar.models.Ingredient;

import java.util.List;

public class DishIngredientsAdapter extends RecyclerView.Adapter<DishIngredientsAdapter.ViewHolder> {
    Context context;
    private int key;
    List<DishIngredient> ingredients;
    public List<Ingredient> allingredients;
    LayoutInflater inflater;
    private boolean isNameCorrect;
    private OnButtonClickListener listener;
    private boolean isQuantityCorrect;
    private RecyclerView attachedRV;

    ArrayAdapter<Ingredient> ingredientsAdapter;

    public DishIngredientsAdapter (int key, Context context, OnButtonClickListener listener, List<DishIngredient> ingredients, List<Ingredient> allingredients) {
        this.context = context;
        this.key = key;
        this.ingredients = ingredients;
        this.listener = listener;
        this.allingredients = allingredients;
        inflater = LayoutInflater.from(context);
        isNameCorrect = false;
        isQuantityCorrect = false;
        if (this.ingredients.size() > 1) {
            for (int i = 0; i < this.ingredients.size() - 1; i++) {
                this.ingredients.get(i).SetIsAdded(true);
                allingredients.remove(ingredients.get(i));
            }
        }
    }

    public List<DishIngredient> GetIngredients() {
        ingredients.remove(ingredients.size() - 1);
        return this.ingredients;
    }

    public interface OnButtonClickListener {
        public void onAddIngredientClick(DishIngredient d, int position);
        public void onDeleteIngredientClick(DishIngredient d, int position, DishIngredientsAdapter adapter, RecyclerView rv_dish_ingredients);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        attachedRV = recyclerView;
    }

    @NonNull
    @Override
    public DishIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.dish_ingredient_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishIngredientsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setlayout(ingredients.get(i));
        viewHolder.setData(ingredients.get(i));
        viewHolder.setClickListeners(ingredients.get(i), i);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout toView;
        private RelativeLayout toEdit;
        private TextView nameToView;
        private TextView quantityToView;
        private AutoCompleteTextView nameToEdit;
        private EditText quantityToEdit;
        private ImageButton ingredient_accept_but;
        private ImageButton ingredient_delete_but;

        private Ingredient ingredient;
        private String oldName = "";

        ViewHolder(View v) {
            super(v);
            toView = (RelativeLayout) v.findViewById(R.id.ingredient_to_view);
            toEdit = (RelativeLayout) v.findViewById(R.id.ingredient_to_edit);
            nameToView = (TextView) v.findViewById(R.id.ingredient_name_to_view);
            quantityToView = (TextView) v.findViewById(R.id.ingredient_quantity_to_view);
            nameToEdit = (AutoCompleteTextView) v.findViewById(R.id.ingredient_name_to_edit);
            quantityToEdit = (EditText) v.findViewById(R.id.ingredient_quantity_to_edit);
            ingredient_accept_but = (ImageButton) v.findViewById(R.id.ingredient_accept_but);
            ingredient_delete_but = (ImageButton) v.findViewById(R.id.ingredient_delete_but);

            ingredientsAdapter = new ArrayAdapter<Ingredient>(context, android.R.layout.simple_list_item_1, allingredients);
            nameToEdit.setAdapter(ingredientsAdapter);
            nameToEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ingredient = (Ingredient) parent.getItemAtPosition(position);
                    oldName = ingredient.GetName();
                    isNameCorrect = true;
                    nameToEdit.setTextColor(Color.BLACK);
                }
            });

            nameToEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(oldName)) {
                        isNameCorrect = false;
                        nameToEdit.setTextColor(Color.RED);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });

            quantityToEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().isEmpty()) isQuantityCorrect = false;
                    else isQuantityCorrect = true;
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });
        }

        public void setClickListeners(DishIngredient d, final int position) {
            final DishIngredient dishIngredient = d;
            ingredient_accept_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNameCorrect && isQuantityCorrect) {
                        ingredients.get(ingredients.size() - 1).SetQuantity(quantityToEdit.getText().toString());
                        ingredients.get(ingredients.size() - 1).SetIngredientId(ingredient.GetId());
                        ingredients.get(ingredients.size() - 1).SetIngredient(ingredient);
                        ingredients.get(ingredients.size() - 1).SetIsAdded(true);

                        nameToEdit.setEnabled(false);
                        quantityToEdit.setEnabled(false);
                        isNameCorrect = false; isQuantityCorrect = false;
                        oldName = "";
                        ingredient_delete_but.setVisibility(View.VISIBLE);
                        ingredient_accept_but.setVisibility(View.GONE);
                        ingredients.add(new DishIngredient(-1, -1, -1, "", new Ingredient(""), false));
                        notifyItemInserted(ingredients.size() - 1);
                        allingredients.remove(ingredient);
                        //notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, "Введены не все данные", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ingredient_delete_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteIngredientClick(dishIngredient, position, DishIngredientsAdapter.this, attachedRV);
                }
            });
        }

        public void setlayout(DishIngredient d) {
            switch(key) {
                case 0://просмотр
                    toView.setVisibility(View.VISIBLE);
                    toEdit.setVisibility(View.GONE);
                    break;
                case 1://добавление
                case 2:
                    toView.setVisibility(View.GONE);
                    toEdit.setVisibility(View.VISIBLE);
                    if (d.GetIsAdded()) {
                        ingredient_accept_but.setVisibility(View.GONE);
                        ingredient_delete_but.setVisibility(View.VISIBLE);
                        nameToEdit.setEnabled(false);
                        nameToEdit.setTextColor(Color.BLACK);
                        quantityToEdit.setEnabled(false);
                    }
                    else {
                        ingredient_accept_but.setVisibility(View.VISIBLE);
                        ingredient_delete_but.setVisibility(View.GONE);
                        nameToEdit.setEnabled(true);
                        quantityToEdit.setEnabled(true);
                        ingredientsAdapter = new ArrayAdapter<Ingredient>(context, android.R.layout.simple_list_item_1, allingredients);
                        nameToEdit.setAdapter(ingredientsAdapter);
                    }
                    break;
            }
        }

        public void setData(DishIngredient d) {
            switch(key) {
                case 0://просмотр
                    nameToView.setText(d.GetIngredient().GetName());
                    quantityToView.setText(d.GetQuantity());
                    break;
                case 1:
                case 2://редактирование
                    oldName = d.GetIngredient().GetName();
                    nameToEdit.setText(d.GetIngredient().GetName());
                    quantityToEdit.setText(d.GetQuantity());
                    break;
            }
        }
    }
}
