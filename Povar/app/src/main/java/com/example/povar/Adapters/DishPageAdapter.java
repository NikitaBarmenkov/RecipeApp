package com.example.povar.Adapters;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class DishPageAdapter extends PagerAdapter {
    List<View> pages = null;

    public DishPageAdapter(List<View> pages){
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(View collection, int position){
        View v = pages.get(position);
        ((ViewPager) collection).addView(v, 0);
        return v;
    }

    @Override
    public int getCount(){
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1){
    }

    @Override
    public Parcelable saveState(){
        return null;
    }
}
