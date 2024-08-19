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
import com.example.food4you.Activity.ListFoodsActivity;
import com.example.food4you.Models.Category;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


//view holder is the object that connect the view to java
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewholder> {

     ArrayList<Category> items ; //data
     Context context ;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    //holds references to the views in each item of the RecyclerView
    public class CategoryViewholder extends RecyclerView.ViewHolder{

        private TextView titleText ;
        private ImageView pic ;


        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);

            titleText =itemView.findViewById(R.id.Category_name);
            pic = itemView.findViewById(R.id.Category_image);
        }
    }

    //responsible for creating a new ViewHolder object
    //inflate the layout for each item in the recycle view return the view holder
    @NonNull
    @Override
    public CategoryAdapter.CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category , parent ,false); //
        return new CategoryViewholder(view);
    }

    //this method binds the data to the ViewHolder at specified position
    //the position parameter indicate the position of the item within the adapter data set
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewholder holder, @SuppressLint("RecyclerView") int position) {

        //set the category name in the text view
        holder.titleText.setText(items.get(position).getName());

        //set a specific background resource based on the position
        switch (position){
            case 0:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_1);
                break;
            }
            case 1:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_2);
                break;
            }
            case 2:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_3);
                break;
            }
            case 3:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_4);
                break;
            }
            case 4:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_5);
                break;
            }
            case 5:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_6);
                break;
            }
            case 6:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_7);
                break;
            }
            case 7:{
                holder.pic.setBackgroundResource(R.drawable.category_circle_8);
                break;
            }
        }

        //get the drawable resource ID for the category image
        @SuppressLint("DiscouragedApi") int drawableResources = context.getResources().getIdentifier(items.get(position).getImagePath()
                , "drawable",holder.itemView.getContext().getPackageName());

        //load image using glide into the image view
        Glide.with(context)
                .load(drawableResources)
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context , ListFoodsActivity.class);
            intent.putExtra("categoryId" , items.get(position).getId());
            intent.putExtra("categoryName" , items.get(position).getName());
            context.startActivity(intent);

        });
    }
    //this method return the total number of items in the data set held by the adapter
    @Override
    public int getItemCount() {
        return items.size();
    }
}
