package com.example.food4you.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food4you.Helper.ManagmentCart;
import com.example.food4you.Helper.TinyDB;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.viewholder> {

    ArrayList<Integer> pricesList;
    private ManagmentCart managmentCart ;
    private ArrayList<ArrayList<Foods>> listOfLists;
    private Context context;

    public OrderHistoryAdapter(ArrayList<ArrayList<Foods>> listOfLists, Context context) {
        this.listOfLists = listOfLists;
        this.context = context;
        managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_history, parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        //get the list of foods for the current position
        ArrayList<Foods> currentFoodsList = listOfLists.get(position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.InnerRecyclerView.getContext(),LinearLayoutManager.VERTICAL,false);
        holder.InnerRecyclerView.setLayoutManager(linearLayoutManager);

        //create and set the adapter for the inner RecyclerView
        InnerAdapter innerAdapter = new InnerAdapter(currentFoodsList, context);
        holder.InnerRecyclerView.setAdapter(innerAdapter);

        pricesList = TinyDB.getInstance().getIntList("TotalPrice");
        holder.totalPrice.setText("$" + pricesList.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfLists.size();
    }


    public class viewholder extends RecyclerView.ViewHolder{

        private TextView totalPrice;
        private RecyclerView InnerRecyclerView;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            totalPrice = itemView.findViewById(R.id.totalPrice);
            InnerRecyclerView = itemView.findViewById(R.id.InnerRecyclerView);
        }
    }
}
