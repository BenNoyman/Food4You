package com.example.food4you.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    //daclare firebaseAuth and firebaseDatabase instance
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init database
        firebaseDatabase = FirebaseDatabase.getInstance();
        //init auth
        firebaseAuth = FirebaseAuth.getInstance();

    }
}