package com.example.food4you.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food4you.Adapter.CategoryAdapter;
import com.example.food4you.Callbacks.CategoryCallback;
import com.example.food4you.Helper.DataManager;
import com.example.food4you.Models.Category;
import com.example.food4you.R;

import java.util.ArrayList;


public class CategoryFragment extends Fragment{

    private RecyclerView Main_category_RView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        Main_category_RView = view.findViewById(R.id.Main_category_RView);

        DataManager.getInstance().setCategoryCallback(new CategoryCallback() {
            @Override
            public void onCategoryDataLoaded(ArrayList<Category> categoryList) {
                if (categoryList.size() > 0) {
                    Main_category_RView.setLayoutManager(new GridLayoutManager(getContext(), 4));
                    RecyclerView.Adapter adapter = new CategoryAdapter(categoryList);
                    Main_category_RView.setAdapter(adapter);
                    Main_category_RView.setVisibility(View.VISIBLE);
                }
            }
        });
        DataManager.getInstance().initCategory();
        return view;
    }
}