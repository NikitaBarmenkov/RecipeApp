package com.example.povar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.povar.models.Category;
import com.example.povar.models.Dish;
import com.example.povar.models.DishIngredient;
import com.example.povar.models.Ingredient;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBRepository implements Serializable {
    private static transient DataBaseHelper dbHelper;
    private static transient SQLiteDatabase database;

    public DBRepository() {}

    public DBRepository(Context context){
        dbHelper = new DataBaseHelper(context);

        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(String TableName){
        Cursor data = null;
        String[] columns = null;
        switch (TableName){
            case "Category":
                columns = new String[] {"_id", "name"};
                break;
            case "Dish":
                columns = new String[] {"_id", "name", "rating", "image", "kkal", "category_id"};
                break;
            case "DishIngredient":
                columns = new String[] {"_id", "dish_id", "ingredient_id", "quantity"};
                break;
            case "Ingredient":
                columns = new String[] {"_id", "name"};
                break;
        }
        //data = database.query(TableName, columns, null, null, null, null, null);
        data = database.rawQuery(String.format("SELECT * FROM %s", String.valueOf(TableName)), null);
        return data;
    }

    public List<Category> getCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = getAllEntries("Category");
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                List<Dish> categoryDishes = getDishesById(id);
                categories.add(new Category(id, name, categoryDishes));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public Category getCategory(long id){
        Category category = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", "Category", "_id");
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            List<Dish> dishes = getDishesById(id);
            category = new Category((int)id, name, dishes);
        }
        cursor.close();
        return  category;
    }

    public List<Dish> getDishes(){
        ArrayList<Dish> dishes = new ArrayList<>();
        Cursor cursor = getAllEntries("Dish");
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                double kkal = cursor.getDouble(cursor.getColumnIndex("kkal"));
                String recipe = cursor.getString(cursor.getColumnIndex("recipe"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                dishes.add(new Dish(id, name, rating, image, kkal, recipe, category_id));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dishes;
    }

    public List<DishIngredient> getDishIngredientsByDishId(long id){
        ArrayList<DishIngredient> ingredients = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s=?", "DishIngredient", "dish_id");
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            do{
                int dish_id = cursor.getInt(cursor.getColumnIndex("dish_id"));
                int ingredient_id = cursor.getInt(cursor.getColumnIndex("ingredient_id"));
                String quantity = cursor.getString(cursor.getColumnIndex("quantity"));
                Ingredient i = getIngredient(ingredient_id);
                ingredients.add(new DishIngredient((int)id, dish_id, ingredient_id, quantity, i, false));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }

    public List<Dish> getDishesToSearch(int which, int ingredient_id, String dish_name){
        ArrayList<Dish> dishes = new ArrayList<>();
        Cursor cursor = null;
        switch(which) {
            case 0://
                cursor = null;
                break;
            case 1://поиск по ингредиетам и названию
                cursor = database.rawQuery(String.format("SELECT Dish._id, Dish.name, Dish.rating, Dish.image, Dish.kkal, Dish.recipe, Dish.category_id" +
                        " FROM Dish, DishIngredient WHERE Dish._id = DishIngredient.dish_id AND DishIngredient.ingredient_id = %s AND Dish.name like \'%%%s%%\'", String.valueOf(ingredient_id), String.valueOf(dish_name)), null);
                break;
            case 2://поиск по названию
                cursor = database.rawQuery(String.format("SELECT * FROM Dish WHERE Dish.name like \'%%%s%%\'", String.valueOf(dish_name)), null);
                break;
            case 3://только поиск по ингредиентам
                cursor = database.rawQuery(String.format("SELECT Dish._id, Dish.name, Dish.rating, Dish.image, Dish.kkal, Dish.recipe, Dish.category_id FROM Dish, DishIngredient WHERE Dish._id = DishIngredient.dish_id AND DishIngredient.ingredient_id = %s", String.valueOf(ingredient_id)), null);
                break;
        }
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                double kkal = cursor.getDouble(cursor.getColumnIndex("kkal"));
                String recipe = cursor.getString(cursor.getColumnIndex("recipe"));
                int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
                dishes.add(new Dish(id, name, rating, image, kkal, recipe, category_id));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dishes;
    }

    public List<Dish> getDishesById(long category_id){
        ArrayList<Dish> dishes = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s=?", "Dish", "category_id");
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(category_id)});
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                double kkal = cursor.getDouble(cursor.getColumnIndex("kkal"));
                String recipe = cursor.getString(cursor.getColumnIndex("recipe"));
                dishes.add(new Dish(id, name, rating, image, kkal, recipe, (int)category_id));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dishes;
    }

    public Dish getDishByName(String name){
        Dish dish = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", "Dish", "name");
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(name)});
        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
            double kkal = cursor.getDouble(cursor.getColumnIndex("kkal"));
            String recipe = cursor.getString(cursor.getColumnIndex("recipe"));
            int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
            dish = new Dish(id, name, rating, image, kkal, recipe, category_id);
        }
        cursor.close();
        return dish;
    }

    public Dish getDish(long id){
        Dish dish = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", "Dish", "_id");
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
            double kkal = cursor.getDouble(cursor.getColumnIndex("kkal"));
            String recipe = cursor.getString(cursor.getColumnIndex("recipe"));
            int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));
            dish = new Dish((int)id, name, rating, image, kkal, recipe, category_id);
        }
        cursor.close();
        return  dish;
    }

    public long getCountDish(){
        return DatabaseUtils.queryNumEntries(database, "Dish");
    }

    public long insertDish(Dish dish){
        ContentValues cv = new ContentValues();
        cv.put("name", dish.GetName());
        cv.put("rating", dish.GetRating());
        cv.put("image", dish.GetImage());
        cv.put("kkal", dish.GetKkal());
        cv.put("recipe", dish.GetRecipe());
        cv.put("category_id", dish.GetCategory());

        return  database.insert("Dish", null, cv);
    }

    public long insertDishIngredient(DishIngredient di){
        ContentValues cv = new ContentValues();
        cv.put("dish_id", di.GetDishId());
        cv.put("ingredient_id", di.GetIngredientId());
        cv.put("quantity", di.GetQuantity());

        return  database.insert("DishIngredient", null, cv);
    }

    public long deleteDishIngredient(long id){
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete("DishIngredient", whereClause, whereArgs);
    }

    public long deleteDish(long id){
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete("Dish", whereClause, whereArgs);
    }

    public long updateDish(Dish dish){
        String whereClause = "_id" + "=" + String.valueOf(dish.GetId());
        ContentValues cv = new ContentValues();
        cv.put("name", dish.GetName());
        cv.put("rating", dish.GetRating());
        cv.put("image", dish.GetImage());
        cv.put("kkal", dish.GetKkal());
        cv.put("recipe", dish.GetRecipe());
        cv.put("category_id", dish.GetCategory());
        return database.update("Dish", cv, whereClause, null);
    }

    public List<Ingredient> getIngredients(){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = getAllEntries("Ingredient");
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                ingredients.add(new Ingredient(id, name));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }

    public Ingredient getIngredient(long id){
        Ingredient ingredient = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", "Ingredient", "_id");
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            ingredient = new Ingredient((int)id, name);
        }
        cursor.close();
        return ingredient;
    }
}
