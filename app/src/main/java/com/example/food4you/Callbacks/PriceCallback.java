package com.example.food4you.Callbacks;

import com.example.food4you.Models.Location;
import com.example.food4you.Models.Price;

import java.util.ArrayList;

public interface PriceCallback {
    void onPriceDataLoaded(ArrayList<Price> PriceList);
}
