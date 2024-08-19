package com.example.food4you.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.food4you.Callbacks.LocationUpdateCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationManager {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static volatile LocationManager instance = null;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private PermissionCallback permissionCallback;
    private double latitude;
    private double longitude;
    private int selectedLocationIndex = 0;

    private LocationUpdateCallback locationUpdateCallback;

    public LocationManager(Context context, LocationUpdateCallback locationUpdateCallback) {
        this.context = context;
        this.locationUpdateCallback = locationUpdateCallback;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static LocationManager getInstance(){
        return  instance;
    }

    public static LocationManager init(Context context, LocationUpdateCallback locationUpdateCallback){
        if(instance == null){
            synchronized (LocationManager.class){
                if(instance == null)
                    instance = new LocationManager(context,locationUpdateCallback);
            }
        }
        if(locationUpdateCallback != null)
            instance.setLocationUpdateCallback(locationUpdateCallback);

        return getInstance();
    }

    public void setLocationUpdateCallback(LocationUpdateCallback callback) {
        this.locationUpdateCallback = callback;
    }

    // Request location updates from the FusedLocationProviderClient
    public void getDeviceLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(10000); // 10 seconds
                locationRequest.setFastestInterval(5000); // 5 seconds

                // Define a LocationCallback to handle location results
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int index = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(index).getLatitude();
                            longitude = locationResult.getLocations().get(index).getLongitude();
                            Log.d("location", "onLocationResult: lat:" + latitude + " lon:" + longitude);

                            if (locationUpdateCallback != null) {
                                locationUpdateCallback.onLocationUpdated(latitude,longitude);
                            }
                            stopLocationUpdates();
                        }
                    }
                };

                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        }
    }

    // Request location permission from the user
    public void requestLocationPermission(Activity activity, PermissionCallback callback) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            // Store the callback to be called later
            this.permissionCallback = callback;
        } else {
            // Permission already granted
            if (callback != null) {
                callback.onPermissionResult(true);
            }
        }
    }

    // Handle the result of the permission request
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permissionCallback != null) {
                permissionCallback.onPermissionResult(granted);
                permissionCallback = null;  // Clear the callback
            }
        }
    }

    public void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public interface PermissionCallback {
        void onPermissionResult(boolean granted);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setSelectedLocationIndex(int index) {
        this.selectedLocationIndex = index;
    }

    public int getSelectedLocationIndex() {
        return selectedLocationIndex;
    }
}
