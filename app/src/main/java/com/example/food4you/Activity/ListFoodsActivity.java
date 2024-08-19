package com.example.food4you.Activity;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food4you.Adapter.FoodListAdapter;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {
    private ImageView backBtn;
    private TextView titleText;
    private RecyclerView FoodListRView;

    private RecyclerView.Adapter adapterListFood ;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_foods);

        findViews();
        getIntentExtra();
        initList();
    }

    private void initList() {
        DatabaseReference myRef = firebaseDatabase.getReference("Foods");
        ArrayList<Foods> foodsArrayList = new ArrayList<>();

        Query query ;

        if (isSearch){  //check if its a search operation
            query = myRef.orderByChild("title").startAt(searchText).endAt(searchText + '\uf8ff');    //query to find items where the "Title" starts with the search text.
        }                                                                                                       //'\uf8ff' character is a special Unicode character used in Firebase
        else {                                                                                                  //queries to create a range that includes all possible values for a given property
            query = myRef.orderByChild("categoryId").equalTo(categoryId);    // query to find items where the "CategoryId" matches the specified categoryId
  
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        foodsArrayList.add(issue.getValue(Foods.class));
                    }
                    if (foodsArrayList.size()> 0){
                        FoodListRView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this , 2));  //set grid with 2 columns
                        adapterListFood = new FoodListAdapter(foodsArrayList);
                        FoodListRView.setAdapter(adapterListFood);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("categoryId" , 0);
        categoryName = getIntent().getStringExtra("categoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch",false);

        titleText.setText(categoryName);
        backBtn.setOnClickListener(v -> finish());
    }

    private void findViews() {
        backBtn = findViewById(R.id.backBtn);
        titleText = findViewById(R.id.titleText);
        FoodListRView = findViewById(R.id.FoodListRView);
    }
}