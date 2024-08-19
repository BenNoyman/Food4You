package com.example.food4you.Callbacks;

import com.example.food4you.Models.Foods;

import java.util.ArrayList;

public interface FavoritesCallback {
    void onFavoritesDataLoaded(ArrayList<Foods> FoodsList);
}
