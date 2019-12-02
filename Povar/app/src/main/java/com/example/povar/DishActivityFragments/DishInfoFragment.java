package com.example.povar.DishActivityFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.povar.DBRepository;
import com.example.povar.R;
import com.example.povar.models.Category;
import com.example.povar.models.Dish;

import java.util.List;

public class DishInfoFragment extends Fragment {
    Spinner spinner;
    List<Category> categories;
    Context context;
    private int key;
    DBRepository db;
    Dish dish;
    OnSpinnerListener listener;

    EditText dishpage_name_to_edit;
    TextView dishpage_name_to_view;
    TextView dishpage_category_text;
    Button add_dish_picture_but;
    ImageView dishpage_picture;

    public DishInfoFragment() {}

    public static DishInfoFragment newInstance() {
        return new DishInfoFragment();
    }

    public interface OnSpinnerListener {
        public void OnCategoryClick(Category category);
        public void NameChanged(String dish_name);
        public void AddDishPictureClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_info_fragment, container, false);
        GetBundle();
        SetupView(view);
        SetData(view);
        return view;
    }

    private void SetData(View v) {
        try {
            categories = db.getCategories();
        }
        catch(Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Bitmap bmp;
        switch(key) {
            case 0:
                dishpage_name_to_view.setText(dish.GetName());
                dishpage_category_text.setText(db.getCategory(dish.GetCategory()).GetName());
                bmp = BitmapFactory.decodeByteArray(dish.GetImage(), 0, dish.GetImage().length);
                dishpage_picture.setImageBitmap(bmp);
                break;
            case 1:
            case 2:
                dishpage_name_to_edit.setText(dish.GetName());
                bmp = BitmapFactory.decodeByteArray(dish.GetImage(), 0, dish.GetImage().length);
                dishpage_picture.setImageBitmap(bmp);
                SetupSpinner(v);
                break;
            default:
                break;
        }
    }

    public void SetOnSpinnerListener(OnSpinnerListener listener) {
        this.listener = listener;
    }

    private void GetBundle() {
        Bundle args = this.getArguments();
        key = args.getInt("key");
        db = (DBRepository) args.getSerializable("db");
        dish = (Dish) args.getSerializable("dish");
    }

    private void SetupView(View v) {
        dishpage_name_to_view = (TextView) v.findViewById(R.id.dishpage_name_to_view);
        dishpage_name_to_edit = (EditText) v.findViewById(R.id.dishpage_name_to_edit);
        dishpage_category_text = (TextView) v.findViewById(R.id.dishpage_category_text);
        spinner = (Spinner) v.findViewById(R.id.category_spinner);
        add_dish_picture_but = (Button) v.findViewById(R.id.add_dish_picture_but);
        dishpage_picture = (ImageView) v.findViewById(R.id.dishpage_picture);
        dishpage_picture.setClipToOutline(true);

        add_dish_picture_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.AddDishPictureClick();
            }
        });
        switch(key) {
            case 0:
                dishpage_name_to_view.setVisibility(View.VISIBLE);
                dishpage_name_to_edit.setVisibility(View.GONE);
                dishpage_category_text.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                add_dish_picture_but.setVisibility(View.GONE);
                break;
            case 1:
            case 2:
                dishpage_name_to_view.setVisibility(View.GONE);
                dishpage_name_to_edit.setVisibility(View.VISIBLE);
                dishpage_category_text.setVisibility(View.GONE);
                add_dish_picture_but.setVisibility(View.VISIBLE);
                TextListener();
                break;
            default:
                break;
        }
    }

    public void TextListener(){
        dishpage_name_to_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    listener.NameChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void SetupSpinner(View v) {
        spinner.setVisibility(View.VISIBLE);

        ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Category category, category1;
        if (dish.GetCategory() != -1){
            category = db.getCategory(dish.GetCategory());
            for (int i = 0; i < spinner.getCount(); i++) {
                category1 = (Category) spinner.getItemAtPosition(i);
                if (category1.GetId() == category.GetId()) {
                    spinner.setSelection(i);
                }
            }
        }
        else {
            spinner.setSelection(1);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.OnCategoryClick(categories.get(position));
                ((TextView) view).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
