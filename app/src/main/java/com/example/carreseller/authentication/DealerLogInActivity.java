package com.example.carreseller.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.carreseller.R;

public class DealerLogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_log_in);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}