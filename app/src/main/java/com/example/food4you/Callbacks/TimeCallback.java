package com.example.food4you.Callbacks;

import com.example.food4you.Models.Location;
import com.example.food4you.Models.Time;

import java.util.ArrayList;

public interface TimeCallback {
    void onTimeDataLoaded(ArrayList<Time> TimeList);
}
