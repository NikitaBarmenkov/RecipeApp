package com.example.povar;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.povar.Adapters.CategoryDataAdapter;
import com.example.povar.Adapters.SearchIngredientsAdapter;
import com.example.povar.Adapters.SearchResultsAdapter;
import com.example.povar.models.Category;
import com.example.povar.models.Dish;
import com.example.povar.models.Ingredient;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchIngredientsAdapter.OnIngredientListener, SearchResultsAdapter.OnDishListener{

    DBRepository db;
    //List<Dish> dishes;
    RecyclerView mList;
    RelativeLayout IngredientsSearch;
    RecyclerView recyclerView;//категории блюд
    RecyclerView rec;//ингредиенты в поиске
    SearchResultsAdapter mAdapter;
    SearchIngredientsAdapter searchIngredientsAdapter;
    MaterialSearchView mSearchView;
    Button NewDishBut;

    List<Category> categories;
    public List<Dish> alldishes;
    List<Ingredient> searchingredients;
    List<Ingredient> allingredients;

    //значение строки поиска по названию
    String dish_search_name = "";

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //открытие новой активити для добавления нового блюда
        NewDishBut = findViewById(R.id.fab);
        NewDishBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DishActivity.class);
                intent.putExtra("key", 1);
                MainActivity.this.startActivity(intent);
            }
        });

        //разметка для поиска ингредиентов
        IngredientsSearch = (RelativeLayout) findViewById(R.id.ingredientsSearch);
        IngredientsSearch.setVisibility(View.GONE);

        db = new DBRepository(getApplicationContext());
        alldishes = db.getDishes();
        categories = db.getCategories();
        allingredients = db.getIngredients();
        searchingredients = new ArrayList<>();

        //определение листа поиска по названию
        mList = (RecyclerView) findViewById(R.id.search_list);
        mList.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CategoryDataAdapter adapter = new CategoryDataAdapter(this, categories);
        adapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);

        //Определение тулбара
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setSearchResultsAdapter();
        StartIngredientsSearch();
    }

    public void setSearchResultsAdapter() {
        //Назначение листу адаптера
        mAdapter = new SearchResultsAdapter(this, this, alldishes, db);
        mList.setAdapter(mAdapter);
    }

    //создание адаптера для поиска ингредиентов
    public void StartIngredientsSearch() {
        rec = (RecyclerView) findViewById(R.id.recycle_view_ingredients_search);
        searchingredients.add(new Ingredient(""));
        searchIngredientsAdapter = new SearchIngredientsAdapter(this, allingredients, searchingredients);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setJustifyContent(JustifyContent.FLEX_START);
        manager.setAlignItems(AlignItems.FLEX_START);
        //manager.setAlignContent(AlignContent.FLEX_START);
        rec.setLayoutManager(manager);
        rec.setAdapter(searchIngredientsAdapter);
        searchIngredientsAdapter.setOnIngredientClickListener(this);
    }

    //вызывается при добавлении ингредиента для поиска
    @Override
    public void onIngredientAction() {
        mAdapter.SetDataToFilter(searchIngredientsAdapter.GetDataToActivity(), dish_search_name);
        //mAdapter.getFilter().filter("");
        mAdapter.getMyFilter();
    }

    //клик по блюду в списке поиска
    @Override
    public void onDishClick(Dish dish) {
        Intent myIntent = new Intent(MainActivity.this, DishActivity.class);
        myIntent.putExtra("dish", dish);
        myIntent.putExtra("key", 0);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mSearchView.setMenuItem(mSearch);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*if(mAdapter.getItemCount() == 1) {
                    Dish d = (Dish) mAdapter.getItem(0);
                    Intent myIntent = new Intent(MainActivity.this, DishActivity.class);
                    myIntent.putExtra("name", d.GetName());
                    myIntent.putExtra("image", d.GetImage());
                    MainActivity.this.startActivity(myIntent);
                }*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dish_search_name = newText;
                if (searchIngredientsAdapter.GetDataToActivity().size() == 1) {
                    searchingredients = new ArrayList<Ingredient>();
                    mAdapter.SetDataToFilter(searchingredients, dish_search_name);
                }
                else mAdapter.SetDataToFilter(searchIngredientsAdapter.GetDataToActivity(), dish_search_name);
                mAdapter.getMyFilter();

                return true;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                IngredientsSearch.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                mList.setVisibility(View.VISIBLE);
                alldishes = db.getDishes();
                NewDishBut.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                IngredientsSearch.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                mList.setVisibility(View.GONE);
                NewDishBut.setVisibility(View.VISIBLE);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
