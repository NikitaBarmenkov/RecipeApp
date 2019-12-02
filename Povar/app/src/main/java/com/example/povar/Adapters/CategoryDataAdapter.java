package com.example.povar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povar.DBRepository;
import com.example.povar.DishActivity;
import com.example.povar.R;
import com.example.povar.models.Category;
import com.example.povar.models.Dish;

import java.util.List;

public class CategoryDataAdapter extends RecyclerView.Adapter<CategoryDataAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Category> categories;
    private Context context;
    private DishDataAdapter horizontalAdapter;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public CategoryDataAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }
    @NonNull
    @Override
    public CategoryDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.homepage_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryDataAdapter.ViewHolder holder, int position) {
        holder.nameView.setText(categories.get(position).GetName());

        horizontalAdapter = new DishDataAdapter(context, categories.get(position).GetDishes(), new DishDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Dish dish) {
                if(dish == null) {
                    Toast.makeText(context,"dish is null",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(context,"okey",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(context, DishActivity.class);
                    myIntent.putExtra("dish", dish);
                    myIntent.putExtra("key", 0);
                    context.startActivity(myIntent);
                }
            }
        });

        horizontalAdapter.setHasStableIds(true);
        holder.categoryRecycleView.setAdapter(horizontalAdapter);

        holder.categoryRecycleView.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView categoryRecycleView;
        private LinearLayoutManager manager = new
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        private TextView nameView;

        ViewHolder(View view){
            super(view);

            categoryRecycleView = view.findViewById(R.id.list_dishes);
            categoryRecycleView.setHasFixedSize(true);
            categoryRecycleView.setNestedScrollingEnabled(false);
            categoryRecycleView.setLayoutManager(manager);
            categoryRecycleView.setItemAnimator(new DefaultItemAnimator());
            categoryRecycleView.setHasFixedSize(true);
            categoryRecycleView.setItemViewCacheSize(20);
            categoryRecycleView.setDrawingCacheEnabled(true);
            categoryRecycleView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            nameView = (TextView) view.findViewById(R.id.category_name);
        }
    }
}

