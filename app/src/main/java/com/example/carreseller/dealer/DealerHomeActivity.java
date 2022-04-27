package com.example.carreseller.dealer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.carreseller.R;
import com.example.carreseller.dealer.chats.ChatsFragment;
import com.example.carreseller.dealer.home.DealerHomeFragment;
import com.example.carreseller.dealer.profile.ProfileFragment;
import com.example.carreseller.dealer.upload.UploadActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DealerHomeActivity extends AppCompatActivity {

    private Fragment selectorFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navigation_home_d :
                    selectorFragment = new DealerHomeFragment();
                    break;

                case R.id.navigation_upload_d :
                    selectorFragment = null;
                    startActivity(new Intent(DealerHomeActivity.this,  UploadActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    break;

                case R.id.navigation_chats_d :
                    selectorFragment = new ChatsFragment();
                    break;

                case R.id.navigation_profile_d :
                    selectorFragment = new ProfileFragment();
                    break;
            }

            if (selectorFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
            }

            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_home_d);
    }
}