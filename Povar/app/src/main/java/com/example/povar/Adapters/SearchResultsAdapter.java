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

import com.example.povar.DBRepository;
import com.example.povar.R;
import com.example.povar.models.Dish;
import com.example.povar.models.DishIngredient;
import com.example.povar.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private Context context;
    public List<Dish> dishes;

    public List<Ingredient> searchIngredients;
    private String dish_search_name;

    OnDishListener listener;
    private LayoutInflater inflater;
    private DBRepository db;

    public SearchResultsAdapter() {}

    public SearchResultsAdapter(Context context, OnDishListener listener, List<Dish> dishes, DBRepository dbRepository) {
        this.context = context;
        this.dishes = dishes;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        this.db = dbRepository;
    }

    public void SetDataToFilter(List<Ingredient> searchIngredients, String name) {
        this.searchIngredients = searchIngredients;
        this.dish_search_name = name;
    }

    public void getMyFilter() {
        ArrayList<Dish> tempList = new ArrayList<Dish>();
        int which = -1;
        if (searchIngredients.isEmpty() && dish_search_name.isEmpty()) {
            which = 0;
        }
        else if (!searchIngredients.isEmpty() && !dish_search_name.isEmpty()) {
            which = 1;
            for (int i = 0; i < searchIngredients.size(); i++) {
                tempList.addAll(db.getDishesToSearch(which, searchIngredients.get(i).GetId(), dish_search_name));
            }
        }
        else if (searchIngredients.isEmpty() && !dish_search_name.isEmpty()) {
            which = 2;
            tempList.addAll(db.getDishesToSearch(which, 0, dish_search_name));
        }
        else if (!searchIngredients.isEmpty() && dish_search_name.isEmpty()) {
            which = 3;
            for (int i = 0; i < searchIngredients.size(); i++) {
                tempList.addAll(db.getDishesToSearch(which, searchIngredients.get(i).GetId(), dish_search_name));
            }
        }
        if (tempList == null) tempList = new ArrayList<Dish>();

        dishes = (ArrayList<Dish>) tempList;
        RemoveClones();
        notifyDataSetChanged();
    }

    public List<Dish> RemoveClones() {
        for (int i = 0; i < dishes.size() - 1; i++) {
            for (int j = i + 1; j < dishes.size(); j++) {
                if (dishes.get(i).GetName().equals(dishes.get(j).GetName())) {
                    dishes.remove(j);
                }
            }
        }
        return dishes;
    }

    public interface OnDishListener {
        public void onDishClick(Dish dish);
    }

    @NonNull
    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.dish_search_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.dishName.setText(dishes.get(i).GetName());
        final Dish d = dishes.get(i);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDishClick(d);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dishName;

        ViewHolder(View v) {
            super(v);
            dishName = (TextView) v.findViewById(R.id.dish_search_name);
        }
    }
}