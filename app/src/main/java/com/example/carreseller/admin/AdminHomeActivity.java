package com.example.carreseller.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.carreseller.R;
import com.example.carreseller.StartActivity;
import com.example.carreseller.admin.category.AddCategoryActivity;
import com.example.carreseller.admin.category.RemoveCategoryActivity;
import com.google.android.material.card.MaterialCardView;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        MaterialCardView addCategory = findViewById(R.id.add_category);
        MaterialCardView removeCategory = findViewById(R.id.remove_category);

        logOut = findViewById(R.id.btn_logout);

        logOut.setOnClickListener(this);
        addCategory.setOnClickListener(this);
        removeCategory.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.add_category:
                intent = new Intent(AdminHomeActivity.this, AddCategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.remove_category:
                intent = new Intent(AdminHomeActivity.this, RemoveCategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                editor.putString("isLogin", "false");
                editor.commit();
                intent = new Intent(AdminHomeActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}