package com.example.food4you.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food4you.Helper.ManagmentCart;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;

import java.util.ArrayList;
import java.util.List;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.viewholder> {
    private List<Foods> foodsList;
    private ManagmentCart managmentCart ;
    private Context context;

    public InnerAdapter(ArrayList<Foods> list, Context context){
        this.foodsList = list;
        this.context = context;
        managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public InnerAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_inner, parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Foods foodItem = foodsList.get(position);

        holder.foodName.setText(foodItem.getTitle());
        holder.foodPrice.setText("$" + foodItem.getPrice());
        holder.itemCount.setText(foodItem.getNumberInCart() + " items");
        //holder.orderDateTime.setText(foodItem.getTimeValue()+ "");

        Glide.with(context)
                .load(foodItem.getImagePath()) //assuming getImagePath() returns a valid URL
                .transform(new CenterCrop() , new RoundedCorners(20))
                .into(holder.foodImage);

        //in the future
        /*holder.leaveReviewButton.setOnClickListener(v -> {
            // Handle leave review click
        });

        holder.orderAgainButton.setOnClickListener(v -> {
            // Handle order again click
        });*/
    }


    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    static class viewholder extends RecyclerView.ViewHolder {
        private ImageView foodImage;
        private TextView foodName, orderDateTime, orderStatus, foodPrice, itemCount;
        private Button leaveReviewButton, orderAgainButton;

        viewholder(View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            orderDateTime = itemView.findViewById(R.id.orderDateTime);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            itemCount = itemView.findViewById(R.id.itemCount);
            leaveReviewButton = itemView.findViewById(R.id.leaveReviewButton);
            orderAgainButton = itemView.findViewById(R.id.orderAgainButton);
        }

    }
}
