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
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


//view holder is the object that connect the view to java
public class BestFoodAdapter extends RecyclerView.Adapter<BestFoodAdapter.BestFoodViewholder> {

     ArrayList<Foods> items ; //data
     Context context ;

    public BestFoodAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    //holds references to the views in each item of the RecyclerView
    public class BestFoodViewholder extends RecyclerView.ViewHolder{

        private MaterialTextView titleText ;
        private TextView priceText;
        private TextView timeText;
        private TextView starText;
        private ImageView pic ;


        public BestFoodViewholder(@NonNull View itemView) {
            super(itemView);

            titleText =itemView.findViewById(R.id.Card_title_text);
            priceText =itemView.findViewById(R.id.Card_price_text);
            timeText =itemView.findViewById(R.id.Card_time_text);
            starText = itemView.findViewById(R.id.Card_star_text);
            pic = itemView.findViewById(R.id.Card_food_image);
        }
    }

    //responsible for creating a new ViewHolder object
    //
    @NonNull
    @Override
    public BestFoodAdapter.BestFoodViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_best_deal , parent ,false); //
        return new BestFoodViewholder(view);
    }

    //this method binds the data to the ViewHolder at specified position
    //the position parameter indicate the position of the item within the adapter data set
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BestFoodAdapter.BestFoodViewholder holder, int position) {

        holder.titleText.setText(items.get(position).getTitle());
        holder.priceText.setText("$" + items.get(position).getPrice());
        holder.timeText.setText(items.get(position).getTimeValue() + "min");
        holder.starText.setText("" + items.get(position).getStar());

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop() , new RoundedCorners(20))
                .into(holder.pic);


        holder.itemView.setOnClickListener(v -> {
            Intent intent  = new Intent(context , DetailActivity.class);
            intent.putExtra("object" , items.get(position));
            context.startActivity(intent);
        });
    }

    //this method return the total number of items in the data set held by the adapter
    @Override
    public int getItemCount() {
        return items.size();
    }


}
