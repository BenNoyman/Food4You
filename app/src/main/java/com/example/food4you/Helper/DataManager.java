package com.example.food4you.Helper;

import androidx.annotation.NonNull;
import com.example.food4you.Callbacks.BestFoodCallback;
import com.example.food4you.Callbacks.CategoryCallback;
import com.example.food4you.Callbacks.FavoritesCallback;
import com.example.food4you.Callbacks.LocationCallback;
import com.example.food4you.Callbacks.PriceCallback;
import com.example.food4you.Callbacks.TimeCallback;
import com.example.food4you.Models.Category;
import com.example.food4you.Models.Foods;
import com.example.food4you.Models.Location;
import com.example.food4you.Models.Price;
import com.example.food4you.Models.Time;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataManager {

    private static DataManager instance;
    private final FirebaseDatabase firebaseDatabase;
    private CategoryCallback categoryCallback;
    private BestFoodCallback bestFoodCallback;
    private LocationCallback locationCallback;
    private TimeCallback timeCallback;
    private PriceCallback priceCallback;
    private FavoritesCallback favoritesCallback;


    private DataManager() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // Initialize and fetch category data from Firebase
    public void initCategory() {
        DatabaseReference myRef = firebaseDatabase.getReference("Category");
        ArrayList<Category> categoryList = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        categoryList.add(issue.getValue(Category.class));
                    }
                    categoryCallback.onCategoryDataLoaded(categoryList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Initialize and fetch best food data from Firebase
    public void initBestFood() {
        DatabaseReference myRef = firebaseDatabase.getReference("Foods");
        ArrayList<Foods> bestFoodList = new ArrayList<>();
        Query query = myRef.orderByChild("bestFood").equalTo(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        bestFoodList.add(issue.getValue(Foods.class));
                    }
                    bestFoodCallback.onBestFoodDataLoaded(bestFoodList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Initialize and fetch favorite foods data from Firebase
    public void initFavoritesFoods() {
        DatabaseReference myRef = firebaseDatabase.getReference("Foods");
        ArrayList<Foods> favoritesList = new ArrayList<>();
        Query query = myRef.orderByChild("favorite").equalTo(true); // Query for favorite food items

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        favoritesList.add(issue.getValue(Foods.class));
                    }
                    favoritesCallback.onFavoritesDataLoaded(favoritesList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Initialize and fetch location data from Firebase
    public void initLocation() {

        DatabaseReference myRef = firebaseDatabase.getReference("Location");    //reference to the location node in firebase (where the location data stored)
        ArrayList<Location> locationList = new ArrayList<>();   //array list created to store the location

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {     //read the data at location node exactly one time (fetch the data once and then stop listening for changes)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){     //check if the data exist in the database
                    for (DataSnapshot issue : snapshot.getChildren()){      //run on every child node of the location
                        locationList.add(issue.getValue(Location.class));       //convert each child into a location object and add it to the list
                    }
                    locationCallback.onLocationDataLoaded(locationList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Initialize and fetch time data from Firebase
    public void initTime() {

        DatabaseReference myRef = firebaseDatabase.getReference("Time");
        ArrayList<Time> timeList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        timeList.add(issue.getValue(Time.class));
                    }
                    timeCallback.onTimeDataLoaded(timeList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Initialize and fetch price data from Firebase
    public void initPrice() {

        DatabaseReference myRef = firebaseDatabase.getReference("Price");
        ArrayList<Price> priceList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        priceList.add(issue.getValue(Price.class));
                    }
                    priceCallback.onPriceDataLoaded(priceList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Setters for callbacks to notify data changes
    public void setFavoritesCallback(FavoritesCallback favoritesCallback) {
        this.favoritesCallback = favoritesCallback;
    }

    public void setPriceCallback(PriceCallback priceCallback) {
        this.priceCallback = priceCallback;
    }

    public void setTimeCallback(TimeCallback timeCallback) {
        this.timeCallback = timeCallback;
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    public void setBestFoodCallback(BestFoodCallback bestFoodCallback) {
        this.bestFoodCallback = bestFoodCallback;
    }

    public void setCategoryCallback(CategoryCallback categoryCallback) {
        this.categoryCallback = categoryCallback;
    }
}
