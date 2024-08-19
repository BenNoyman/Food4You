package com.example.food4you.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.food4you.Adapter.BestFoodAdapter;
import com.example.food4you.Callbacks.BestFoodCallback;
import com.example.food4you.Helper.DataManager;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;

import java.util.ArrayList;


public class BestFoodsFragment extends Fragment {

    private RecyclerView Main_best_food_RView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_best_foods, container, false);

        Main_best_food_RView = view.findViewById(R.id.Main_best_food_RView);

        DataManager.getInstance().setBestFoodCallback(new BestFoodCallback() {
            @Override
            public void onBestFoodDataLoaded(ArrayList<Foods> FoodsList) {
                if (FoodsList.size() > 0){   //check if we have any food items in the list
                    Main_best_food_RView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    RecyclerView.Adapter adapter = new BestFoodAdapter(FoodsList);
                    Main_best_food_RView.setAdapter(adapter);
                    Main_best_food_RView.setVisibility(View.VISIBLE);
                }
            }
        });
        DataManager.getInstance().initBestFood();
        return view;
    }
}