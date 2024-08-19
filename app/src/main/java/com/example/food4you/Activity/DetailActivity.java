package com.example.food4you.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import com.bumptech.glide.Glide;
import com.example.food4you.Helper.ManagmentCart;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends BaseActivity {

    private Foods foodObject;
    private int num = 1;
    private ManagmentCart managmentCart;

    private ImageView backButton;
    private ImageView foodImage;
    private TextView titleText;
    private TextView priceText;
    private TextView descriptionText;
    private TextView ratingText;
    private RatingBar ratingBar;
    private TextView totalPriceText;
    private TextView plusButton;
    private TextView minusButton;
    private TextView quantityNum;
    private AppCompatButton addButton;
    private ImageView favoriteButton;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViews();
        getIntentExtra();
        initDetails();
    }

    private void initDetails() {

        managmentCart = new ManagmentCart(this);

        backButton.setOnClickListener(v -> finish());

        Glide.with(DetailActivity.this).load(foodObject.getImagePath()).into(foodImage);    //load image

        //set the text and rating values from the food object
        titleText.setText(foodObject.getTitle());
        priceText.setText("$" + foodObject.getPrice());
        descriptionText.setText(foodObject.getDescription());
        ratingText.setText(" " + foodObject.getStar() + " Rating");
        ratingBar.setRating((float) foodObject.getStar());
        totalPriceText.setText("$"+(num * foodObject.getPrice()));

        //increase the quantity and update the UI
        plusButton.setOnClickListener(v -> {
            num = num + 1 ;
            quantityNum.setText(num + "");
            totalPriceText.setText("$" + (num * foodObject.getPrice()));
        });

        //decrease the quantity and update the UI
        minusButton.setOnClickListener(v -> {
            if (num != 0){
                num = num -1;
                quantityNum.setText(num + "");
                totalPriceText.setText("$" + (num * foodObject.getPrice()));

            }
        });
        //add the food item to the cart when the add button is clicked
        addButton.setOnClickListener(v -> {
            foodObject.setNumberInCart(num);
            managmentCart.insertFood(foodObject);
        });

        //set the favorite button icon based on the foods favorite status
        if (foodObject.isFavorite()){
            favoriteButton.setImageResource(R.drawable.favorite_white_fill);
        }
        else{
            favoriteButton.setImageResource(R.drawable.favorite_white);
        }

        //reference the specific food item in firebase based on its ID
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods"); //reference to the Foods node in the firebases
        DatabaseReference foodReference = databaseReference.child(String.valueOf(foodObject.getId())).child("favorite");    //reference to the specific food items favorite field in the database

        //fetch the favorite status from firebase when the foodObject is loaded
        foodReference.addListenerForSingleValueEvent(new ValueEventListener() { //fetch the favorite status from firebase when the foodObject is loaded
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    boolean isFavorite = snapshot.getValue(Boolean.class);  //retrieve the current favorite status from Firebase
                    foodObject.setFavorite(isFavorite); //update the status
                    if (isFavorite){
                        favoriteButton.setImageResource(R.drawable.favorite_white_fill);
                    }
                    else{
                        favoriteButton.setImageResource(R.drawable.favorite_white);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //toggle the favorite status and update firebase when the favorite button is clicked
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = foodObject.isFavorite();
                if (isFavorite) {
                    foodObject.setFavorite(false);//mark the food as not favorite
                    favoriteButton.setImageResource(R.drawable.favorite_white);
                    foodReference.setValue(false); //update the favorite status in firebase
                }
                else {
                    foodObject.setFavorite(true);
                    favoriteButton.setImageResource(R.drawable.favorite_white_fill);
                    foodReference.setValue(true);
                }
            }
        });
    }

    private void getIntentExtra() {

        foodObject = (Foods) getIntent().getSerializableExtra("object");

    }

    private void findViews() {

        backButton = findViewById(R.id.backButton);
        foodImage = findViewById(R.id.foodImage);
        titleText = findViewById(R.id.titleText);
        priceText = findViewById(R.id.priceText);
        descriptionText = findViewById(R.id.descriptionText);
        ratingText= findViewById(R.id.ratingText);
        ratingBar= findViewById(R.id.ratingBar);
        totalPriceText= findViewById(R.id.totalPriceText);
        plusButton = findViewById(R.id.plusButton);
        minusButton =findViewById(R.id.minusButton);
        quantityNum  = findViewById(R.id.quantityNum);
        addButton =findViewById(R.id.addButton);
        favoriteButton = findViewById(R.id.favoriteButton);

    }
}