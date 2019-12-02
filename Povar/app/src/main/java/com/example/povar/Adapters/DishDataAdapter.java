package com.example.povar.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.povar.DBRepository;
import com.example.povar.R;
import com.example.povar.models.Dish;

import java.util.List;

public class DishDataAdapter extends RecyclerView.Adapter<DishDataAdapter.ViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(Dish dish);
    }

    private LayoutInflater inflater;
    private List<Dish> dishes;
    private OnItemClickListener listener;
    Context context;
    DBRepository db;
    public double dd;
    String string;
    int image;

    public DishDataAdapter(Context context, List<Dish> dishes, OnItemClickListener listener) {
        this.context = context;
        this.dishes = dishes;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        db = new DBRepository(context);
    }

    @Override
    public DishDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dish_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishDataAdapter.ViewHolder holder, int position) {
        holder.bind(dishes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView;
        private ImageButton delBut;
        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.dish_image);
            nameView = (TextView) view.findViewById(R.id.dish_name);
            delBut = (ImageButton) view.findViewById(R.id.dish_del);
        }

        public void bind(final Dish dish, final OnItemClickListener listener) {
            nameView.setText(dish.GetName());
            imageView.setClipToOutline(true);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(dish.GetImage(), 0, dish.GetImage().length));
            delBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "элемент удален", Toast.LENGTH_SHORT).show();
                    dishes.remove(dish);
                    db.deleteDish(dish.GetId());
                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(dish);
                }
            });
        }
    }
}
