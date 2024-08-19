package com.example.food4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food4you.Adapter.CartAdapter;
import com.example.food4you.Callbacks.ChangeNumberItemsListener;
import com.example.food4you.Helper.ManagmentCart;
import com.example.food4you.Helper.TinyDB;
import com.example.food4you.Models.Foods;
import com.example.food4you.R;
import com.example.food4you.Utilities.SignalManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {

    private RecyclerView.Adapter adapter;   //adapter for managing the cart item in the recycle view
    private ManagmentCart managmentCart;    //helper class for managing cart operations
    private double tax ;    //variable to store calculate tax
    private double total ;
    private Boolean flagCoupon = false;
    private final String coupon = "BenHagever123";

    private ImageView Cart_back_button;
    private TextView totalFeeText;
    private TextView deliveryPriceText;
    private TextView totalTaxText;
    private TextView totalPriceText;
    private TextView emptyText;
    private ScrollView scrollViewCard;
    private RecyclerView Cart_RView;
    private AppCompatButton Cart_PlaceOrder;
    private AppCompatButton ApplyCoupon;
    private EditText Cart_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Gson gson = new Gson();

        managmentCart =new ManagmentCart(this);
        findViews();
        initViews();
        calculateCart();
        initList();
    }

    //init the list of cart items in the recycle view
    private void initList() {
        if (managmentCart.getListCart().isEmpty()){ //check if the cart is empty
            emptyText.setVisibility(View.VISIBLE);  //show empty massage
            scrollViewCard.setVisibility(View.GONE);    //hide cart details
        }
        else {
            emptyText.setVisibility(View.GONE);
            scrollViewCard.setVisibility(View.VISIBLE);
        }

        //set up the recycle view with a vertical LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        Cart_RView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(new ChangeNumberItemsListener() { //create and set the adapter for the recycle view
                    @Override
                    public void change() {

                        calculateCart();    //recalculate cart when items change
                    }
                }, managmentCart.getListCart(), this);

        Cart_RView.setAdapter(adapter);

        Cart_PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Foods> cartItems = managmentCart.getListCart();
                ArrayList<ArrayList<Foods>> listOfLists = new ArrayList<>();
                listOfLists.add(cartItems);
                TinyDB.getInstance().putListOfLists("OrderHistory", listOfLists , (int) total);

                managmentCart.clearCart();
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    //calculate the carts total cost, tax, and delivery
    private void calculateCart() {
        double percentTax = 0.02;   //2%
        double delivery = 10 ;  //dollar

        //calculate the tax and round to two decimal places
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100 ;

        //calculate the total amount and round to two decimal places
         total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100.0) / 100 ;

        totalFeeText.setText("$" + itemTotal);
        totalTaxText.setText("$" + tax);
        deliveryPriceText.setText("$" + delivery);
        totalPriceText.setText("$" + total);
    }

    private void initViews() {

        Cart_back_button.setOnClickListener(v -> finish()); //finish the activity and back to the previous activity

        ApplyCoupon.setOnClickListener(new View.OnClickListener() { //click listener for the apply coupon btn
            @Override
            public void onClick(View v) {
                String inputCoupon = Cart_coupon.getText().toString().trim();   //get the coupon code entered by the user
                if (inputCoupon.equals(coupon) && !flagCoupon){ //check if the input match the coupon and that the coupon has not already been used
                    int discountPrice = (int) (total * 0.2);    //calculate with 20% discount
                    int priceAfterDiscount =(int) total - discountPrice;    //calc the price after applying the discount
                    String discountedPriceString = String.valueOf(priceAfterDiscount);  //convert into string
                    totalPriceText.setText("$" +discountedPriceString );    //update the total price
                    flagCoupon = true;  //set flag to true indicate that the coupon has been applied
                    SignalManager.getInstance().toast("Code matched!");
                }
                else {
                    SignalManager.getInstance().toast("Code did not match. please try again");
                }
            }
        });
    }

    private void findViews() {
        Cart_back_button = findViewById(R.id.Cart_back_button);
        totalFeeText = findViewById(R.id.totalFeeText);
        deliveryPriceText = findViewById(R.id.deliveryPriceText);
        totalTaxText= findViewById(R.id.totalTaxText);
        totalPriceText= findViewById(R.id.totalPriceText);
        emptyText= findViewById(R.id.emptyText);
        scrollViewCard = findViewById(R.id.scrollViewCard);
        Cart_RView = findViewById(R.id.Cart_RView);
        Cart_PlaceOrder = findViewById(R.id.Cart_PlaceOrder);
        ApplyCoupon =findViewById(R.id.ApplyCoupon);
        Cart_coupon = findViewById(R.id.Cart_coupon);
    }
}