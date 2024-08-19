package com.example.food4you.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.food4you.Callbacks.LocationUpdateCallback;
import com.example.food4you.Models.Location;
import com.example.food4you.R;
import com.example.food4you.Utilities.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity implements OnMapReadyCallback, RoutingListener {

    private static final int RETRY_DELAY_MS = 5000; // Delay before retrying
    private static final int MAX_RETRY_ATTEMPTS = 3; // Maximum number of retry attempts

    private GoogleMap map;
    private Marker restaurantMarker;
    private Marker customerMarker;
    private double restaurantLatitude;
    private double restaurantLongitude;
    private double latitude;
    private double longitude;
    private List<Polyline> polylines;
    private int retryCount = 0;

    private static final int[] COLORS = new int[]{R.color.Orange, R.color.category_green, R.color.Orange, R.color.main_color, R.color.category_dark_green};
    private TextView Order_Address;
    private TextView Order_delivery_time;
    private ProgressBar progressBar;
    private AppCompatButton returnHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        findViews();
        returnHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
            startActivity(intent);
        });
        polylines = new ArrayList<>();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        getRoute();
    }

    //initialize the restaurant location by fetching it from firebase database
    private void initRestaurantLocation() {
        DatabaseReference myRef = firebaseDatabase.getReference("Location");
        int selectedIndex = LocationManager.getInstance().getSelectedLocationIndex();

        myRef.child(String.valueOf(selectedIndex)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Location location = snapshot.getValue(Location.class);
                    if (location != null) {
                        restaurantLatitude = location.getLatitude();
                        restaurantLongitude = location.getLongitude();
                        updateRestaurantMarker();
                        drawRoute();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void findViews() {
        Order_Address = findViewById(R.id.Order_Address);
        Order_delivery_time = findViewById(R.id.Order_delivery_time);
        progressBar = findViewById(R.id.progress_bar);
        returnHomeButton = findViewById(R.id.returnHomeButton);
    }

    //start the process of fetching the route between restaurant and customer
    public void getRoute() {
        //set a callback to listen for location updates
        LocationManager.getInstance().setLocationUpdateCallback(new LocationUpdateCallback() {
            @Override
            public void onLocationUpdated(double latitude, double longitude) {
                Log.d("OrderActivity", "Location Updated: Lat = " + latitude + ", Lon = " + longitude); // Add log
                OrderActivity.this.latitude = latitude;
                OrderActivity.this.longitude = longitude;
                updateCustomerMarker();
                drawRoute();
            }
        });
        LocationManager.getInstance().getDeviceLocation();
        initRestaurantLocation();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        //initialize markers if coordinates are available
        updateRestaurantMarker();
        updateCustomerMarker();
    }

    //update the marker for the restaurant on the map
    private void updateRestaurantMarker() {
        if (map != null && restaurantLatitude != 0 && restaurantLongitude != 0) {
            LatLng restaurantLatLng = new LatLng(restaurantLatitude, restaurantLongitude);
            Log.d("OrderActivity", "Updating Restaurant Marker: Lat = " + restaurantLatitude + ", Lon = " + restaurantLongitude); // Add log

            if (restaurantMarker == null) {
                restaurantMarker = map.addMarker(new MarkerOptions().position(restaurantLatLng).title("Restaurant"));
            } else {
                restaurantMarker.setPosition(restaurantLatLng);
            }
        }
    }

    //update the marker for the customer on the map
    private void updateCustomerMarker() {
        if (map != null && latitude != 0 && longitude != 0) {
            LatLng customerLatLng = new LatLng(latitude, longitude);
            Log.d("OrderActivity", "Updating Customer Marker: Lat = " + latitude + ", Lon = " + longitude); // Add log

            if (customerMarker == null) {
                customerMarker = map.addMarker(new MarkerOptions().position(customerLatLng).title("Customer"));
            } else {
                customerMarker.setPosition(customerLatLng);
            }
        }
    }

    //draw the route on the map between the restaurant and the customer
    private void drawRoute() {
        if (map == null) {
            Log.d("OrderActivity", "Map is not ready.");
            return;
        }

        if (restaurantLatitude == 0 || restaurantLongitude == 0) {
            Log.d("OrderActivity", "Restaurant coordinates are not set.");
            return;
        }

        if (latitude == 0 || longitude == 0) {
            Log.d("OrderActivity", "Customer coordinates are not set.");
            return;
        }

        LatLng restaurantLatLng = new LatLng(restaurantLatitude, restaurantLongitude);
        LatLng customerLatLng = new LatLng(latitude, longitude);

        Log.d("OrderActivity", "Drawing route from restaurant: " + restaurantLatLng + " to customer: " + customerLatLng);

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING) //set travel mode to driving
                .withListener(this) //set the listener for routing callbacks
                .alternativeRoutes(true)    //enable alternative routes
                .waypoints(restaurantLatLng, customerLatLng)    //set the start and end points
                .key("API-KEY")
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (retryCount < MAX_RETRY_ATTEMPTS) {  //handle routing failure with retry logic
            retryCount++;
            Log.d("OrderActivity", "Retrying route calculation. Attempt: " + retryCount);
            new Handler().postDelayed(() -> {
                drawRoute(); // Retry fetching the route
            }, RETRY_DELAY_MS);
        } else {
            progressBar.setVisibility(View.GONE); // Hide progress bar if maximum retries reached
            Toast.makeText(this, "Error: " + (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRoutingStart() {
        progressBar.setVisibility(View.VISIBLE); //show progress bar when routing starts
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        progressBar.setVisibility(View.GONE); //hide progress bar on routing success

        if (polylines != null) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        //reset the polylines list
        polylines = new ArrayList<>();

        //check if the route list is not empty
        if (route != null && route.size() > 0) {
            //get the first route
            Route firstRoute = route.get(0);

            //create polyline options for the first route
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[0])); //use the first color
            polyOptions.width(10); //set a fixed width for the polyline
            polyOptions.addAll(firstRoute.getPoints()); //add points from the first route
            Polyline polyline = map.addPolyline(polyOptions); //add polyline to the map
            polylines.add(polyline); //add to polylines list for future reference

            Order_delivery_time.setText(String.valueOf(firstRoute.getDurationText()));
            Order_Address.setText(String.valueOf(firstRoute.getEndAddressText()));

            //move the camera to show the route
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(restaurantLatitude, restaurantLongitude));
            builder.include(new LatLng(latitude, longitude));
            LatLngBounds bounds = builder.build();
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
        }
        else {
            progressBar.setVisibility(View.GONE); //hide progress bar if no routes found
        }
    }

    @Override
    public void onRoutingCancelled() {
        progressBar.setVisibility(View.GONE); //hide progress bar if routing is cancelled
    }
}

