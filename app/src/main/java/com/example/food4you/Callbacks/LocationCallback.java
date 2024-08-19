package com.example.food4you.Callbacks;

import com.example.food4you.Models.Foods;
import com.example.food4you.Models.Location;

import java.util.ArrayList;

public interface LocationCallback {
    void onLocationDataLoaded(ArrayList<Location> LocationList);
}
