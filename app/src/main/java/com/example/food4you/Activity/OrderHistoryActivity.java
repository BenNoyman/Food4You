package com.example.food4you.Activity;


import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food4you.Adapter.OrderHistoryAdapter;
import com.example.food4you.Helper.TinyDB;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    ArrayList<ArrayList<Foods>> arrayLists ;
    private RecyclerView.Adapter adapter;
    private TextView title;
    private RecyclerView RViewHistory;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        findViews();
        initViews();
    }

    //initialize the RecyclerView and set up the adapter to display order history
    private void initViews() {
        ArrayList<ArrayList<Foods>> retrievedLists = TinyDB.getInstance().getListOfLists("OrderHistory");   //retrieve  the list of past orders
        adapter = new OrderHistoryAdapter(retrievedLists, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        RViewHistory.setLayoutManager(linearLayoutManager);
        RViewHistory.setAdapter(adapter);

        backBtn.setOnClickListener(v -> finish());

    }

    private void findViews() {
        RViewHistory = findViewById(R.id.RViewHistory);
        title = findViewById(R.id.title);
        backBtn = findViewById(R.id.backBtn);
    }

}