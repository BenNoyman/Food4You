package com.example.food4you.Callbacks;

import com.example.food4you.Models.Category;

import java.util.ArrayList;

public interface CategoryCallback {
    void onCategoryDataLoaded(ArrayList<Category> categoryList);
}
