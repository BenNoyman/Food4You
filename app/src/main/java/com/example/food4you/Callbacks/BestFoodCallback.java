package com.example.food4you.Callbacks;

import com.example.food4you.Models.Category;
import com.example.food4you.Models.Foods;

import java.util.ArrayList;

public interface BestFoodCallback {
    void onBestFoodDataLoaded(ArrayList<Foods> FoodsList);
}
