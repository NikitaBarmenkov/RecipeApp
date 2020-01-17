package com.example.povar.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povar.R;
import com.example.povar.models.Ingredient;

import java.security.acl.NotOwnerException;
import java.util.List;

public class SearchIngredientsAdapter extends RecyclerView.Adapter<SearchIngredientsAdapter.ViewHolder>{
    public interface OnIngredientListener {
        public void onIngredientAction();
    }

    private LayoutInflater inflater;
    private List<Ingredient> allingredients;
    private List<Ingredient> ingredients;
    private Context context;
    private RecyclerView attachedRV;

    private OnIngredientListener Listener;

    public void setOnIngredientClickListener(OnIngredientListener Listener) {
        this.Listener = Listener;
    }

    public SearchIngredientsAdapter(Context context, List<Ingredient> allingredients, List<Ingredient> ingredients) {
        this.context = context;
        this.allingredients = allingredients;
        this.ingredients = ingredients;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        attachedRV = recyclerView;
    }

    @Override
    public SearchIngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ingredient_search_item, parent, false);
        return new ViewHolder(view);
    }

    public List<Ingredient> GetDataToActivity(){
        return ingredients;
    }

    @Override
    public void onBindViewHolder(SearchIngredientsAdapter.ViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AutoCompleteTextView nameView;
        private LinearLayout linlayout;
        private ImageButton delBut;
        private String bufIngredient;

        private View viewItem;
        private AutoCompleteTextView nameViewbuf;
        private LinearLayout linlayoutbuf;
        private ImageButton delButbuf;

        ArrayAdapter<Ingredient> ingredients_to_searchAdapter;
        ViewHolder(View view){
            super(view);
            nameView = (AutoCompleteTextView) view.findViewById(R.id.ingredient_name_text);
            ingredients_to_searchAdapter = new ArrayAdapter<Ingredient>(context, android.R.layout.simple_list_item_1, allingredients);
            nameView.setAdapter(ingredients_to_searchAdapter);
            nameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    linlayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners));
                    delBut.setVisibility(View.VISIBLE);
                    nameView.setEnabled(false);

                    Ingredient i = (Ingredient) parent.getItemAtPosition(position);

                    allingredients.remove(i);
                    ingredients.remove(ingredients.size() - 1);
                    ingredients.add(i);
                    Listener.onIngredientAction();
                    ingredients.add(new Ingredient(""));
                    notifyDataSetChanged();
                }
            });

            linlayout = (LinearLayout) view.findViewById(R.id.ingredient_search_itemview);
            delBut = (ImageButton) view.findViewById(R.id.ingredient_del_but);
        }

        public void bind(final Ingredient i) {
            nameView.setInputType(InputType.TYPE_CLASS_TEXT);
            nameView.setText(i.GetName());

            ingredients_to_searchAdapter = new ArrayAdapter<Ingredient>(context, android.R.layout.simple_list_item_1, allingredients);
            nameView.setAdapter(ingredients_to_searchAdapter);

            delBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewItem = attachedRV.getLayoutManager().findViewByPosition(ingredients.size() - 1);
                    nameViewbuf = (AutoCompleteTextView) viewItem.findViewById(R.id.ingredient_name_text);

                    ingredients.remove(i);
                    allingredients.add(i);
                    ingredients.remove(ingredients.size() - 1);//удалить последний ингредиент, так как он еще не добавлен
                    Listener.onIngredientAction();//поменять предложения в списке поиска
                    ingredients.add(new Ingredient(nameViewbuf.getText().toString()));
                    if(ingredients.isEmpty())
                        ingredients.add(new Ingredient(""));
                    notifyDataSetChanged();
                    viewItem = attachedRV.getLayoutManager().findViewByPosition(ingredients.size() - 1);
                    linlayoutbuf = (LinearLayout) viewItem.findViewById(R.id.ingredient_search_itemview);
                    delButbuf = (ImageButton) viewItem.findViewById(R.id.ingredient_del_but);
                    nameViewbuf = (AutoCompleteTextView) viewItem.findViewById(R.id.ingredient_name_text);

                    linlayoutbuf.setBackground(ContextCompat.getDrawable(context, android.R.color.transparent));
                    delButbuf.setVisibility(View.GONE);
                    nameViewbuf.setEnabled(true);
                }
            });

            nameView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    nameView.setDropDownWidth(200);
                }
            });

            //nameView.setShowSoftInputOnFocus(true);

            nameView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        if (ingredients_to_searchAdapter.getCount() == 1)
                        {
                            linlayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners));
                            delBut.setVisibility(View.VISIBLE);
                            nameView.setEnabled(false);
                            allingredients.remove(i);

                            ingredients.remove(ingredients.size() - 1);
                            ingredients.add(ingredients_to_searchAdapter.getItem(0));
                            Listener.onIngredientAction();
                            ingredients.add(new Ingredient(""));
                            notifyDataSetChanged();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
