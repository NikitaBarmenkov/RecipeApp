package com.example.povar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.povar.models.Ingredient;

import java.util.List;

public class IngredientSuggestionAdapter extends  RecyclerView.Adapter<IngredientSuggestionAdapter.ViewHolder> implements Filterable {
    @Override
    public Filter getFilter() {
        return null;
    }

    public interface OnItemClickListener {
        void onItemClick(Ingredient ingredient);
    }

    private List<Ingredient> ingredients;
    private OnItemClickListener listener;
    private Context context;
    LayoutInflater inflater;

    public IngredientSuggestionAdapter (Context context, List<Ingredient> ingredients, OnItemClickListener listener) {
        this.context = context;
        this.ingredients = ingredients;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public IngredientSuggestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientSuggestionAdapter.ViewHolder viewHolder, int i) {
        viewHolder.t.setText(ingredients.get(i).GetName());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView t;
        public ViewHolder(View v) {
            super(v);
            t = (TextView) v.findViewById(android.R.id.text1);
        }
    }
}
