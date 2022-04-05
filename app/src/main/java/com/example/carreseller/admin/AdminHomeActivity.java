package com.example.carreseller.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.carreseller.R;
import com.example.carreseller.StartActivity;

public class AdminHomeActivity extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        logOut = findViewById(R.id.btn_logout);


        logOut.setOnClickListener(v -> {
            editor.putString("isLogin", "false");
            editor.commit();
            Intent intent;
            intent = new Intent(AdminHomeActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        });
    }
}