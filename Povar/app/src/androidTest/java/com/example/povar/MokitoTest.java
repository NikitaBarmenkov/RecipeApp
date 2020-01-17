package com.example.povar;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import com.example.povar.models.Category;
import com.example.povar.models.Dish;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MokitoTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Mock
    Dish d;

    @Mock
    DBRepository db;

    //Проверка на первую заглавную букву
    //с моком
    @Test
    public void Test1() {
        d = spy(Dish.class);
        String s = "мое блюдо";
        String expected = "Мое блюдо";
        when(d.setFirstLetter(s)).thenReturn("Мое блюдо");
        d.SetName(s);
        assertEquals(expected, d.GetName());
    }

    //Проверка на первую заглавную букву
    //без мока
    @Test
    public void Test2() {
        Dish d = new Dish();
        String expected = "Разносолы";
        d.SetName("разносолы");
        assertEquals(expected, d.GetName());
    }

    //сравнение кол-ва блюд в категории
    //с моком
    @Test
    public void Test3() {
        db = spy(DBRepository.class);
        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();
        int id = 1;//категория основные блюда - 2 блюда

        List<Dish> dishes = new ArrayList<>();
        dishes.add(db.getDish(2));//Рататуй
        dishes.add(db.getDish(3));//Азу по-татарски

        when(db.getDishesById(id)).thenReturn(dishes);
        Category category = db.getCategory(id);
        int count = category.GetDishes().size();
        assertEquals(2, count);
    }

    //сравнение кол-ва блюд в категории
    //без мока
    @Test
    public void Test4() {
        mActivityRule.launchActivity(null);
        Activity activity = getActivityInstance();
        DBRepository db = new DBRepository(activity.getApplicationContext());
        int id = 2;//категория выпечка и десерты - 2 блюда

        int count = db.getCategory(id).GetDishes().size();
        assertEquals(2, count);
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