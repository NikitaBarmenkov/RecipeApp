package com.example.povar;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import com.example.povar.Adapters.SearchResultsAdapter;
import com.example.povar.models.Dish;
import com.example.povar.models.Ingredient;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    //espresso rule which tells which activity to start
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    //поиск по ингредиенту
    @Test
    public void Test1() {

        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();

        DBRepository db = new DBRepository(activity.getApplicationContext());

        //текст для поиска по названию
        String textToType="";

        //Задаю строки для нахождения ингредиентов по именам в бд
        List<String> strings = new ArrayList<>();
        strings.add("Помидоры");

        List<Ingredient> searchingredients = new ArrayList<>();
        searchingredients = GetIngredients(strings, db);//получаю ингредиенты по строкам выше
        List<Dish> alldishes = db.getDishes();//получаю все блюда

        //Ожидаемый результат
        //Задаю строки для нахождения блюд по именам в бд
        List<String> exdishes = new ArrayList<>();
        exdishes.add("Рататуй");
        exdishes.add("Салат Цезарь");
        List<String> dishes = new ArrayList<>();

        //инициализация адаптера
        SearchResultsAdapter adapter = new SearchResultsAdapter(activity.getApplicationContext(), new SearchResultsAdapter.OnDishListener() {
            @Override
            public void onDishClick(Dish dish) { }
        }, alldishes, db);
        adapter.SetDataToFilter(searchingredients, textToType);
        adapter.getMyFilter();//вызов фильтра
        alldishes = adapter.dishes;
        for (int i = 0; i < alldishes.size(); i++) {
            dishes.add(alldishes.get(i).GetName());
        }
        assertEquals(exdishes, dishes);
    }

    //поиск по названию
    @Test
    public void Test2() {

        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();

        DBRepository db = new DBRepository(activity.getApplicationContext());

        //текст для поиска по названию
        String textToType="та";

        //Задаю строки для нахождения ингредиентов по именам в бд
        List<String> strings = new ArrayList<>();

        List<Ingredient> searchingredients = new ArrayList<>();
        //searchingredients = GetIngredients(strings, db);//получаю ингредиенты по строкам выше
        List<Dish> alldishes = db.getDishes();//получаю все блюда

        //Ожидаемый результат
        //Задаю строки для нахождения блюд по именам в бд
        List<String> exdishes = new ArrayList<>();
        exdishes.add("Рататуй");
        exdishes.add("Азу по-татарски");
        List<String> dishes = new ArrayList<>();
        List<Dish> expecteddishes = GetDishes(exdishes, db);//получаю блюда по именам выше

        //инициализация адаптера
        SearchResultsAdapter adapter = new SearchResultsAdapter(activity.getApplicationContext(), new SearchResultsAdapter.OnDishListener() {
            @Override
            public void onDishClick(Dish dish) { }
        }, alldishes, db);
        adapter.SetDataToFilter(searchingredients, textToType);
        adapter.getMyFilter();//вызов фильтра
        alldishes = adapter.dishes;
        for (int i = 0; i < alldishes.size(); i++) {
            dishes.add(alldishes.get(i).GetName());
        }
        assertEquals(exdishes, dishes);
    }

    //поиск по ингредиентам
    @Test
    public void Test11() {

        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();

        DBRepository db = new DBRepository(activity.getApplicationContext());

        //текст для поиска по названию
        String textToType="";

        //Задаю строки для нахождения ингредиентов по именам в бд
        List<String> strings = new ArrayList<>();
        strings.add("Сахар");
        strings.add("Яйцо куриное");

        List<Ingredient> searchingredients = new ArrayList<>();
        searchingredients = GetIngredients(strings, db);//получаю ингредиенты по строкам выше
        List<Dish> alldishes = db.getDishes();//получаю все блюда

        //Ожидаемый результат
        //Задаю строки для нахождения блюд по именам в бд
        List<String> exdishes = new ArrayList<>();
        exdishes.add("Рататуй");
        exdishes.add("Шарлотка");
        //exdishes.add("Тонкие блины на молоке");
        List<String> dishes = new ArrayList<>();
        List<Dish> expecteddishes = GetDishes(exdishes, db);//получаю блюда по именам выше

        //инициализация адаптера
        SearchResultsAdapter adapter = new SearchResultsAdapter(activity.getApplicationContext(), new SearchResultsAdapter.OnDishListener() {
            @Override
            public void onDishClick(Dish dish) { }
        }, alldishes, db);
        adapter.SetDataToFilter(searchingredients, textToType);
        adapter.getMyFilter();//вызов фильтра
        alldishes = adapter.dishes;
        for (int i = 0; i < alldishes.size(); i++) {
            dishes.add(alldishes.get(i).GetName());
        }
        assertEquals(exdishes, dishes);
    }

    //комбинированный поиск по ингредиенту
    @Test
    public void Test3() {

        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();

        DBRepository db = new DBRepository(activity.getApplicationContext());

        //текст для поиска по названию
        String textToType="шар";

        //Задаю строки для нахождения ингредиентов по именам в бд
        List<String> strings = new ArrayList<>();
        strings.add("Пшеничная мука");

        List<Ingredient> searchingredients = new ArrayList<>();
        searchingredients = GetIngredients(strings, db);//получаю ингредиенты по строкам выше
        List<Dish> alldishes = db.getDishes();//получаю все блюда

        //Ожидаемый результат
        //Задаю строки для нахождения блюд по именам в бд
        List<String> exdishes = new ArrayList<>();
        exdishes.add("Шарлотка");
        List<String> dishes = new ArrayList<>();
        List<Dish> expecteddishes = GetDishes(exdishes, db);//получаю блюда по именам выше

        //инициализация адаптера
        SearchResultsAdapter adapter = new SearchResultsAdapter(activity.getApplicationContext(), new SearchResultsAdapter.OnDishListener() {
            @Override
            public void onDishClick(Dish dish) { }
        }, alldishes, db);
        adapter.SetDataToFilter(searchingredients, textToType);
        adapter.getMyFilter();//вызов фильтра
        alldishes = adapter.dishes;
        for (int i = 0; i < alldishes.size(); i++) {
            dishes.add(alldishes.get(i).GetName());
        }
        assertEquals(exdishes, dishes);
    }

    //комбинированный поиск по ингредиентам
    @Test
    public void Test31() {

        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();

        DBRepository db = new DBRepository(activity.getApplicationContext());

        //текст для поиска по названию
        String textToType="о";

        //Задаю строки для нахождения ингредиентов по именам в бд
        List<String> strings = new ArrayList<>();
        strings.add("Сахар");
        strings.add("Яйцо куриное");

        List<Ingredient> searchingredients = new ArrayList<>();
        searchingredients = GetIngredients(strings, db);//получаю ингредиенты по строкам выше
        List<Dish> alldishes = db.getDishes();//получаю все блюда

        //Ожидаемый результат
        //Задаю строки для нахождения блюд по именам в бд
        List<String> exdishes = new ArrayList<>();
        exdishes.add("Шарлотка");
        exdishes.add("Тонкие блины на молоке");
        List<String> dishes = new ArrayList<>();
        List<Dish> expecteddishes = GetDishes(exdishes, db);//получаю блюда по именам выше

        //инициализация адаптера
        SearchResultsAdapter adapter = new SearchResultsAdapter(activity.getApplicationContext(), new SearchResultsAdapter.OnDishListener() {
            @Override
            public void onDishClick(Dish dish) { }
        }, alldishes, db);
        adapter.SetDataToFilter(searchingredients, textToType);
        adapter.getMyFilter();//вызов фильтра
        alldishes = adapter.dishes;
        for (int i = 0; i < alldishes.size(); i++) {
            dishes.add(alldishes.get(i).GetName());
        }
        assertEquals(exdishes, dishes);
    }

    //пустой поиск
    @Test
    public void Test4() {

        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();

        DBRepository db = new DBRepository(activity.getApplicationContext());

        //текст для поиска по названию
        String textToType="";

        //Задаю строки для нахождения ингредиентов по именам в бд
        List<String> strings = new ArrayList<>();

        List<Ingredient> searchingredients = new ArrayList<>();
        //searchingredients = GetIngredients(strings, db);//получаю ингредиенты по строкам выше
        List<Dish> alldishes = db.getDishes();//получаю все блюда

        //Ожидаемый результат
        //Задаю строки для нахождения блюд по именам в бд
        List<String> exdishes = new ArrayList<>();
        List<String> dishes = new ArrayList<>();
        List<Dish> expecteddishes = GetDishes(exdishes, db);//получаю блюда по именам выше

        //инициализация адаптера
        SearchResultsAdapter adapter = new SearchResultsAdapter(activity.getApplicationContext(), new SearchResultsAdapter.OnDishListener() {
            @Override
            public void onDishClick(Dish dish) { }
        }, alldishes, db);
        adapter.SetDataToFilter(searchingredients, textToType);
        adapter.getMyFilter();//вызов фильтра
        alldishes = adapter.dishes;
        for (int i = 0; i < alldishes.size(); i++) {
            dishes.add(alldishes.get(i).GetName());
        }
        assertEquals(exdishes, dishes);
    }

    private List<Ingredient> GetIngredients(List<String> strings, DBRepository db) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients = db.getIngredients();
        boolean may;
        for (int i = 0; i < ingredients.size(); i++) {
            may = false;
            for (int j = 0; j < strings.size(); j++) {
                String name = ingredients.get(i).GetName();
                String str = strings.get(j);
                if ((name.equals(str))) {
                    may = false;
                    break;
                }
                else may = true;
            }
            if (may) {
                ingredients.remove(i);
                i -= 1;
            }
        }
        return ingredients;
    }

    private List<Dish> GetDishes(List<String> strings, DBRepository db) {
        List<Dish> dishes = new ArrayList<>();
        dishes = db.getDishes();
        boolean may;
        for (int i = 0; i < dishes.size(); i++) {
            may = false;
            for (int j = 0; j < strings.size(); j++) {
                String name = dishes.get(i).GetName();
                String str = strings.get(j);
                if ((name.equals(str))) {
                    may = false;
                    break;
                }
                else may = true;
            }
            if (may) {
                dishes.remove(i);
                i -= 1;
            }
        }
        return dishes;
    }

    private Activity getActivityInstance(){
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }
}
