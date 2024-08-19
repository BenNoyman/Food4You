package com.example.food4you.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food4you.Adapter.BestFoodAdapter;
import com.example.food4you.Adapter.FoodListAdapter;
import com.example.food4you.Callbacks.BestFoodCallback;
import com.example.food4you.Callbacks.FavoritesCallback;
import com.example.food4you.Helper.DataManager;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritesActivity extends BaseActivity {
    private ImageView FavbackBtn;
    private TextView titleText;
    private RecyclerView FoodListRView;
    private RecyclerView.Adapter adapterListFood ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        findViews();

        fetchFavoritesFood();

        FavbackBtn.setOnClickListener(v -> finish());
    }

    //fetch the list of favorite foods from the DataManager
    private void fetchFavoritesFood(){

        //set the callback to handle the data once its loaded
        DataManager.getInstance().setFavoritesCallback(new FavoritesCallback() {
            @Override
            public void onFavoritesDataLoaded(ArrayList<Foods> FoodsList) {
                if (FoodsList.size()> 0){
                    FoodListRView.setLayoutManager(new GridLayoutManager(FavoritesActivity.this , 2));  //set grid with 2 columns
                    adapterListFood = new FoodListAdapter(FoodsList);
                    FoodListRView.setAdapter(adapterListFood);
                }
            }
        });
        DataManager.getInstance().initFavoritesFoods(); //init the loading of favorite foods
    }

    private void findViews() {
        FavbackBtn = findViewById(R.id.FavbackBtn);
        titleText = findViewById(R.id.titleText);
        FoodListRView = findViewById(R.id.FoodListRView);
    }
}