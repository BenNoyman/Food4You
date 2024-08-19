package com.example.food4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food4you.Adapter.BestFoodAdapter;
import com.example.food4you.Adapter.CategoryAdapter;
import com.example.food4you.Callbacks.LocationCallback;
import com.example.food4you.Callbacks.PriceCallback;
import com.example.food4you.Callbacks.TimeCallback;
import com.example.food4you.Fragments.BestFoodsFragment;
import com.example.food4you.Fragments.CategoryFragment;
import com.example.food4you.Callbacks.LocationUpdateCallback;
import com.example.food4you.Helper.DataManager;
import com.example.food4you.Helper.ManagmentCart;
import com.example.food4you.Models.Category;
import com.example.food4you.Models.Foods;
import com.example.food4you.Models.Location;
import com.example.food4you.Models.Price;
import com.example.food4you.Models.Time;
import com.example.food4you.R;
import com.example.food4you.Utilities.LocationManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity  {

    private ManagmentCart cartManager;
    private Spinner locationSp;
    private Spinner timeSp;
    private Spinner priceSp;
    private FloatingActionButton Main_signOut_button;
    private EditText Main_searchText;
    private ImageView Main_magnifyingGlass;
    private AppCompatImageView Main_cart_IMG;
    private MaterialTextView Main_username_text;
    private Toolbar Main_nav;
    private DrawerLayout main;
    private NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartManager = new ManagmentCart(this);

        findViews();
        loadLocation();
        loadBestFood();
        loadCategory();
        loadPrice();
        loadTime();
        setVariable();
        initNavigatorBar();
    }

    //method to load location data and set up the location spinner
    private void loadLocation() {
        DataManager.getInstance().setLocationCallback(new LocationCallback() {
            @Override
            public void onLocationDataLoaded(ArrayList<Location> LocationList) {
                ArrayAdapter<Location> arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, LocationList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSp.setAdapter(arrayAdapter);
                //set an item selected listener for the location spinner
                locationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //store the selected index in the singleton
                        LocationManager.getInstance().setSelectedLocationIndex(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
        DataManager.getInstance().initLocation();
    }

    //method to load price data and set up the price spinner
    private void loadPrice(){
        DataManager.getInstance().setPriceCallback(new PriceCallback() {
            @Override
            public void onPriceDataLoaded(ArrayList<Price> PriceList) {
                ArrayAdapter<Price> arrayAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item , PriceList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                priceSp.setAdapter(arrayAdapter);
            }
        });
        DataManager.getInstance().initPrice();
    }

    //method to load time data and set up the time spinner
    private void loadTime(){
        DataManager.getInstance().setTimeCallback(new TimeCallback() {
            @Override
            public void onTimeDataLoaded(ArrayList<Time> TimeList) {
                ArrayAdapter<Time> arrayAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item , TimeList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                timeSp.setAdapter(arrayAdapter);
            }
        });
        DataManager.getInstance().initTime();
    }

    //method to load the Best Foods fragment into the activity
    private void loadBestFood(){
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        fragmentManager1.beginTransaction()
                .replace(R.id.bestFoods_fragment, new BestFoodsFragment())
                .commit();
    }

    //method to load the Category fragment into the activity
    private void loadCategory(){
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        fragmentManager2.beginTransaction()
                .replace(R.id.fragment_container, new CategoryFragment())
                .commit();
    }

    //method to initialize the navigation drawer
    private void initNavigatorBar() {
        Menu menu = nav_view.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_home);
        nav_view.bringToFront();    //bring the navigation view to the front
        setSupportActionBar(Main_nav);  //set the toolbar as the app bar for the activity
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //disable default title display
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,main,Main_nav,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        main.addDrawerListener(toggle); //add drawer toggle listener
        nav_view.setItemIconTintList(null); //disable tinting of item icons
        toggle.syncState(); //synchronize the state of the drawer toggle

        //set a click listener for the toolbar to open the navigation drawer
        Main_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.open();    //open the drawer when the toolbar is clicked
            }
        });

        //set a navigation item selected listener for the navigation view
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                //handle navigation item clicks
                if (itemId == R.id.nav_myOrders){
                    Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
                    startActivity(intent);
                }
                else if (itemId == R.id.nav_favorites) {
                    Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                }
                main.close();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (main.isDrawerOpen(GravityCompat.START)){
            main.closeDrawer(GravityCompat.START);  //close drawer if open
        }
        else{
            super.onBackPressed();
        }
    }

    private void setVariable() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Main_username_text.setText(user.getDisplayName());

        Main_signOut_button.setOnClickListener(v -> {
            //clear the cart
            cartManager.clearCart();

            //sign out from Firebase
            FirebaseAuth.getInstance().signOut();

            //clear AuthUI cached data
            AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
                //clear Google Sign-In cache
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                googleSignInClient.signOut().addOnCompleteListener(this, task1 -> {
                    //navigate to LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish(); //close MainActivity
                });
            });
        });

        Main_magnifyingGlass.setOnClickListener(v -> {
            String text = Main_searchText.getText().toString();
            if (!text.isEmpty()){
                Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
                intent.putExtra("text" , text);
                intent.putExtra("isSearch" , true);
                startActivity(intent);
            }
        });

        Main_cart_IMG.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findViews() {
        Main_username_text = findViewById(R.id.Main_username_text);
        locationSp = findViewById(R.id.locationSp);
        timeSp = findViewById(R.id.timeSp);
        priceSp = findViewById(R.id.dollarSp);
        Main_signOut_button = findViewById(R.id.Main_signOut_button);
        Main_searchText = findViewById(R.id.Main_searchText);
        Main_magnifyingGlass = findViewById(R.id.Main_magnifyingGlass);
        Main_cart_IMG = findViewById(R.id.Main_cart_IMG);
        Main_nav = findViewById(R.id.Main_nav);
        main = findViewById(R.id.main);
        nav_view = findViewById(R.id.nav_view);
    }
}