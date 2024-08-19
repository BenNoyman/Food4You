package com.example.food4you.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.food4you.Callbacks.ChangeNumberItemsListener;
import com.example.food4you.Helper.ManagmentCart;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {

    ArrayList<Foods> list ;
    private ManagmentCart managmentCart ;   //manages the cart operations
    ChangeNumberItemsListener changeNumberItemsListener;    //listener to handle changes in the number of items in the cart

    public CartAdapter(ChangeNumberItemsListener changeNumberItemsListener, ArrayList<Foods> list , Context context) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    //inflate the view for each item in the recycle view
    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent,false);
        return new viewholder(view);
    }

    //binds the data to each viewholder setting the text and image for each item
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        //set the food title , calculate price and quantity
        holder.cartTitleText.setText(list.get(position).getTitle());
        holder.cartFeeEachItem.setText("$" + (list.get(position).getNumberInCart()*list.get(position).getPrice()));
        holder.cartTotalEachItem.setText(list.get(position).getNumberInCart() + " * $" + list.get(position).getPrice());
        holder.cartNumOfItems.setText(list.get(position).getNumberInCart() + "");

        //load the image using glide
        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop() , new RoundedCorners(20))
                .into(holder.cartImage);

        //handle click on the plus button to increase the quantity
        holder.cartPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.plusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void change() {
                        notifyDataSetChanged(); //notify the adapter of changes
                        changeNumberItemsListener.change(); //notify the listener of the changes
                    }
                });
            }
        });

        //handle click on the minus button to increase the quantity
        holder.cartMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managmentCart.minusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void change() {
                        notifyDataSetChanged(); //notify the adapter of changes
                        changeNumberItemsListener.change(); //notify the listener of the changes
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //viewholder class to hold and recycle views as they scroll off the screen
    public class viewholder extends RecyclerView.ViewHolder{
        private ImageView cartImage;
        private TextView cartTitleText;
        private TextView cartTotalEachItem;
        private TextView cartFeeEachItem;
        private TextView cartNumOfItems;
        private TextView cartMinusBtn;
        private TextView cartPlusBtn;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            cartImage = itemView.findViewById(R.id.cartImage);
            cartTitleText = itemView.findViewById(R.id.cartTitleText);
            cartTotalEachItem = itemView.findViewById(R.id.cartTotalEachItem);
            cartFeeEachItem = itemView.findViewById(R.id.cartFeeEachItem);
            cartNumOfItems = itemView.findViewById(R.id.cartNumOfItems);
            cartMinusBtn = itemView.findViewById(R.id.cartMinusBtn);
            cartPlusBtn = itemView.findViewById(R.id.cartPlusBtn);

        }
    }
}
