package com.example.carreseller.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.carreseller.R;
import com.example.carreseller.users.ui.category.CategoryFragment;
import com.example.carreseller.users.ui.chats.ChatFragment;
import com.example.carreseller.users.ui.home.HomeFragment;
import com.example.carreseller.users.ui.notification.NotificationFragment;
import com.example.carreseller.users.ui.profile.ProfileFragment;
import com.example.carreseller.users.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment selectorFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navigation_home :
                    selectorFragment = new HomeFragment();
                    break;

                case R.id.navigation_category :
                    selectorFragment = new CategoryFragment();
                    break;

                case R.id.navigation_chats :
                    selectorFragment = new ChatFragment();
                    break;

                case R.id.navigation_profile :
                    selectorFragment = new ProfileFragment();
                    break;
            }

            if (selectorFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
            }

            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}