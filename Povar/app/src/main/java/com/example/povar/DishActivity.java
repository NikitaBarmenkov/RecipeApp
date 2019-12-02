package com.example.povar;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.povar.Adapters.DishIngredientsAdapter;
import com.example.povar.Adapters.DishPageAdapter;
import com.example.povar.DishActivityFragments.DishInfoFragment;
import com.example.povar.DishActivityFragments.DishIngredientsFragment;
import com.example.povar.DishActivityFragments.DishRecipeFragment;
import com.example.povar.models.Category;
import com.example.povar.models.Dish;
import com.example.povar.models.DishIngredient;
import com.example.povar.models.Ingredient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DishActivity extends AppCompatActivity implements DishIngredientsAdapter.OnButtonClickListener, DishInfoFragment.OnSpinnerListener, DishRecipeFragment.TextListener, DishPictureChoiceDialog.OnClickListeners {

    private Dish dish;
    private List<DishIngredient> ingredients;
    private List<Ingredient> allingredients;
    private static transient DBRepository db;
    private static final int GALLERY_REQUEST_CODE = 1337;
    private static final int CAMERA_PIC_REQUEST = 1336;
    private static final int DIALOG_NEW_PIC = 2344;

    Menu menu;
    int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dish_activity);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //контекст бд для нахождения ингридиентов
        db = new DBRepository(this);
        ingredients = new ArrayList<>();
        allingredients = db.getIngredients();

        //rv_dish_ingredients = findViewById(R.id.rv_dish_ingredients);

        Bundle arguments = getIntent().getExtras();
        key = (int)arguments.get("key");
        switch(key) {
            case 0://просмотр
                dish = (Dish)arguments.getSerializable("dish");
                //получение ингредиентов по id блюда
                ingredients = db.getDishIngredientsByDishId(dish.GetId());
                break;
            case 2://изменение
                dish = (Dish)arguments.getSerializable("dish");
                //получение ингредиентов по id блюда
                ingredients = db.getDishIngredientsByDishId(dish.GetId());
                ingredients.add(new DishIngredient(-1, -1, -1, "", new Ingredient(""), false));
                break;
            case 1://добавление
                dish = new Dish(-1, "", 0, null, 0, "", -1);
                Drawable d = ContextCompat.getDrawable(this, R.drawable.placeholder);
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                dish.SetImage(ConvertBitmapToByte(bitmap));
                ingredients.add(new DishIngredient(-1, -1, -1, "", new Ingredient(""), false));
                break;
            default:
                break;
        }
        navigation.setSelectedItemId(R.id.navigation_info);

        //adapter = SetIngredientsAdapter();
        //rv_dish_ingredients.setAdapter(adapter);

        //initialVewPager(dish.GetImage());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle args = new Bundle();
            args.putInt("key", key);
            args.putSerializable("db", db);
            args.putSerializable("dish", dish);
            switch (item.getItemId()) {
                case R.id.navigation_info:
                    DishInfoFragment dishInfoFragment = new DishInfoFragment();
                    dishInfoFragment.SetOnSpinnerListener(DishActivity.this);
                    loadFragment(dishInfoFragment, args);
                    return true;
                case R.id.navigation_recipe:
                    DishRecipeFragment dishRecipeFragment = new DishRecipeFragment();
                    dishRecipeFragment.SetTextListener(DishActivity.this);
                    loadFragment(dishRecipeFragment, args);
                    return true;
                case R.id.navigation_ingredient:
                    DishIngredientsFragment dishIngredientsFragment = new DishIngredientsFragment();
                    dishIngredientsFragment.SetListener(DishActivity.this);
                    dishIngredientsFragment.SetIngredients(ingredients);
                    loadFragment(dishIngredientsFragment, args);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment, Bundle args) {
        fragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (key == 0) {
            getMenuInflater().inflate(R.menu.dish_menu, menu);
            MenuItem item = menu.getItem(1);
            item.setVisible(false);
            item.setEnabled(false);
        }
        else if (key == 1 || key == 2) {
            getMenuInflater().inflate(R.menu.dish_menu, menu);
            MenuItem item = menu.getItem(0);
            item.setVisible(false);
            item.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_dish && key == 1 && !ingredients.isEmpty()) {//добавить
            ingredients.remove(ingredients.size() - 1);
            db.insertDish(dish);
            dish = db.getDishByName(dish.GetName());
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.get(i).SetDishId(dish.GetId());
                db.insertDishIngredient(ingredients.get(i));
            }
            Intent myIntent = new Intent(DishActivity.this, DishActivity.class);
            myIntent.putExtra("dish", dish);
            myIntent.putExtra("key", 0);
            DishActivity.this.startActivity(myIntent);
            return true;
        }
        else if (id == R.id.action_add_dish && key == 2 && !ingredients.isEmpty()) {//изменить
            ingredients.remove(ingredients.size() - 1);
            db.updateDish(dish);//обновляем блюдо
            dish = db.getDish(dish.GetId());//получаем обновленное блюдо
            List<DishIngredient> d = db.getDishIngredientsByDishId(dish.GetId());//получаем все ингредиенты этого блюда
            for (int i = 0; i < d.size(); i++) {
                db.deleteDishIngredient(d.get(i).GetId());//удаляем все ингредиенты

            }
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.get(i).SetDishId(dish.GetId());//добавляем новый измененый список ингредиентов
                db.insertDishIngredient(ingredients.get(i));
            }
            Intent myIntent = new Intent(DishActivity.this, DishActivity.class);
            myIntent.putExtra("dish", dish);
            myIntent.putExtra("key", 0);
            DishActivity.this.startActivity(myIntent);
            return true;
        }
        else if (id == R.id.action_edit_dish && key == 0) {//запустить изменение
            Intent intent = new Intent(DishActivity.this, DishActivity.class);
            intent.putExtra("dish", dish);
            intent.putExtra("key", 2);
            DishActivity.this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialVewPager(int dish_image) {
        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<View>();

        View page = inflater.inflate(R.layout.dish_image_fragment, null);
        ImageView imageView = (ImageView) page.findViewById(R.id.carousel_image);
        imageView.setImageResource(dish_image);
        pages.add(page);

        page = inflater.inflate(R.layout.dish_image_fragment, null);
        imageView = (ImageView) page.findViewById(R.id.carousel_image);
        imageView.setImageResource(R.drawable.placeholder);
        pages.add(page);

        page = inflater.inflate(R.layout.dish_image_fragment, null);
        imageView = (ImageView) page.findViewById(R.id.carousel_image);
        imageView.setImageResource(R.drawable.placeholder);
        pages.add(page);

        DishPageAdapter pagerAdapter = new DishPageAdapter(pages);
        //ViewPager viewPager = (ViewPager)findViewById(R.id.vpager);
        //viewPager.setAdapter(pagerAdapter);
        //viewPager.setCurrentItem(0);
    }

    @Override
    public void onAddIngredientClick(DishIngredient d, int position) {
    }

    @Override
    public void onDeleteIngredientClick(DishIngredient d, int position, DishIngredientsAdapter adapter, RecyclerView rv_dish_ingredients) {
        ingredients.remove(position);
        //rv_dish_ingredients.removeViewAt(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, ingredients.size());
        adapter.allingredients.add(d.GetIngredient());
    }

    @Override
    public void OnCategoryClick(Category category) {
        dish.SetCategory(category.GetId());
    }

    @Override
    public void NameChanged(String dish_name) {
        dish.SetName(dish_name);
    }

    @Override
    public void AddDishPictureClick() {
        DishPictureChoiceDialog dialog = new DishPictureChoiceDialog();
        dialog.show(getSupportFragmentManager(), "DishPictureChoiceDialog");
    }

    @Override
    public void OnRecipeTextChanged(String text) {
        dish.SetRecipe(text);
    }

    private void pickFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(DishActivity.this.getContentResolver(), data.getData());
                        dish.SetImage(ConvertBitmapToByte(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (requestCode == CAMERA_PIC_REQUEST) {
                if (data != null) {
                    try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        dish.SetImage(ConvertBitmapToByte(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private byte[] ConvertBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void openGaleryClick() {
        pickFromGallery();
    }

    @Override
    public void openCameraClick() {
        openCamera();
    }

    @Override
    public void onBackPressed() {
        if (key == 0 || key == 1) {
            Intent myIntent = new Intent(DishActivity.this, MainActivity.class);
            DishActivity.this.startActivity(myIntent);
        }
        else if (key == 2) {
            Intent myIntent = new Intent(DishActivity.this, DishActivity.class);
            myIntent.putExtra("dish", dish);
            myIntent.putExtra("key", 0);
            DishActivity.this.startActivity(myIntent);
        }
    }
}
