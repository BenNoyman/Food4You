package com.example.food4you.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food4you.Activity.DetailActivity;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;

import java.util.ArrayList;

//adapter class for the recycleView that display a list of foods
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.viewholder> {

    ArrayList<Foods> foodsArrayList;
    Context context;

    // constructor to init the adapter with the list of foods items
    public FoodListAdapter(ArrayList<Foods> foodsArrayList) {
        this.foodsArrayList = foodsArrayList;
    }

    @NonNull
    @Override   //when the recycleView need a new view holder to represent the item its called
    public FoodListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_food_list,parent,false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override   //called by recycleView to display the data at the specified position
    public void onBindViewHolder(@NonNull FoodListAdapter.viewholder holder, int position) {
        //bind the data to the view holder
        holder.title_text.setText(foodsArrayList.get(position).getTitle());
        holder.time_text.setText(foodsArrayList.get(position).getTimeValue() + "min");
        holder.price_text.setText("$" + foodsArrayList.get(position).getPrice());
        holder.rate_text.setText("" + foodsArrayList.get(position).getStar());

        Glide.with(context)     //load the image into the imageView
                .load(foodsArrayList.get(position).getImagePath())
                .transform(new CenterCrop() , new RoundedCorners(20))
                .into(holder.food_image);

        //when item clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent  = new Intent(context , DetailActivity.class);
            intent.putExtra("object" , foodsArrayList.get(position));   //pass the clicked food item to the DetailActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodsArrayList.size();
    }

    //viewHolder class to hold the views for each food item
    public class viewholder extends RecyclerView.ViewHolder {
        TextView title_text;
        TextView time_text;
        TextView price_text;
        TextView rate_text;
        ImageView food_image;

        //init the viewHolder with the item views
        public viewholder(@NonNull View itemView) {
            super(itemView);
            title_text =itemView.findViewById(R.id.title_text);
            time_text = itemView.findViewById(R.id.time_text);
            price_text = itemView.findViewById(R.id.price_text);
            rate_text = itemView.findViewById(R.id.rate_text);
            food_image = itemView.findViewById(R.id.food_image);
        }
    }
}
