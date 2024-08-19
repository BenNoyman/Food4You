package com.example.food4you;

import android.app.Application;

import com.example.food4you.Helper.TinyDB;
import com.example.food4you.Utilities.LocationManager;
import com.example.food4you.Utilities.SignalManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SignalManager.init(this);
        //LocationManager.init(this , null);
        TinyDB.init(this);

    }
}
