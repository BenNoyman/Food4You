package com.example.food4you.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.food4you.Callbacks.LocationUpdateCallback;
import com.example.food4you.R;
import com.example.food4you.Utilities.LocationManager;

public class SplashActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private AppCompatImageView splash_IMG_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        locationManager = LocationManager.init(this, null);
        requestLocationPermission();
    }

    //request location permission and handle the result
    private void requestLocationPermission() {
        locationManager.requestLocationPermission(this, new LocationManager.PermissionCallback() {
            @Override
            public void onPermissionResult(boolean granted) {
                if (granted) {
                    proceedWithAppInitialization(); //proceed with the app init if the permission is granted
                }
            }
        });
    }

    //proceed with initializing the app, such as fetching location and starting animations
    private void proceedWithAppInitialization() {
        getUserLocation();
        findViews();
        startAnimation(splash_IMG_logo);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //get the users current location
    private void getUserLocation() {
        locationManager = LocationManager.getInstance();
        locationManager.setLocationUpdateCallback(new LocationUpdateCallback() {
            @Override
            public void onLocationUpdated(double latitude, double longitude) {
                SplashActivity.this.latitude = latitude;
                SplashActivity.this.longitude = longitude;
            }
        });
        locationManager.getDeviceLocation();
    }

    //start the splash screen animation for the logo
    private void startAnimation(View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        //start location
        view.setY(-height / 2.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f); //opacity

        view.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationY(0)
                .setDuration(4000)
                .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
            }
            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                transactToLoginActivity();
            }
            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }
            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
            }
        });
    }

    private void transactToLoginActivity(){

        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private void findViews() {

        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
    }
}