package com.example.carreseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreseller.dealer.DealerHomeActivity;
import com.example.carreseller.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CarInfoActivity extends AppCompatActivity {

    private ImageView carImageView;

    private TextView carName;
    private TextView company;
    private TextView yearLaunched;
    private TextView seatingCapacity;
    private TextView fuelType;
    private TextView engine;
    private TextView price;
    private TextView category;
    private TextView s_name;

    private String c_id, d_id, name_d, dealer_profile_mage;

    private Button b_chat, b_watch_list, b_remove;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        carImageView = findViewById(R.id.img_car);

        category = findViewById(R.id.c_i_category);
        carName = findViewById(R.id.c_i_name);
        company = findViewById(R.id.c_i_company);
        yearLaunched = findViewById(R.id.c_i_year);
        seatingCapacity = findViewById(R.id.c_i_seats);
        fuelType = findViewById(R.id.c_i_fuel);
        engine = findViewById(R.id.c_i_engine);
        price = findViewById(R.id.c_i_price);
        s_name = findViewById(R.id.s_name);

        b_watch_list = findViewById(R.id.add_to_watch_list);
        b_remove = findViewById(R.id.remove_car);
        b_chat = findViewById(R.id.b_chat);

        Intent intent=getIntent();
        final String sDealerId = intent.getStringExtra("dealer_id");
        final String sCarId = intent.getStringExtra("car_id");
        final String sCarName = intent.getStringExtra("name");
        final String sCompany = intent.getStringExtra("company");
        final String sImage = intent.getStringExtra("image");
        final String sYearLaunched = intent.getStringExtra("year");
        final String sSeatingCapacity = intent.getStringExtra("seating_capacity");
        final String sFuelType = intent.getStringExtra("fuel_type");
        final String sEngine = intent.getStringExtra("engine");
        final String sPrice = intent.getStringExtra("car_price");
        final String sCategory = intent.getStringExtra("category");

        if (sDealerId.equals(firebaseUser.getUid())) {
            b_remove.setVisibility(View.VISIBLE);
        }

        String p = "₹ " + sPrice;
        String seats = "Seating Capacity: " + sSeatingCapacity;
        String launchYear = "Launched Year: " + sYearLaunched;
        String fuel = "Fuel Type: " + sFuelType;
        String e = "Engine: " + sEngine;

        c_id = sCarId;
        d_id = sDealerId;

        carName.setText(sCarName);
        category.setText(sCategory);
        company.setText(sCompany);
//        s_name.setText(book_title);

        price.setText(p);
        fuelType.setText(fuel);
        seatingCapacity.setText(seats);
        yearLaunched.setText(launchYear);
        engine.setText(e);

        getDealerDetails();

        try {
            Picasso.get().load(sImage).placeholder(R.drawable.loading_shape).into(carImageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        checkWatchList();

        b_chat.setOnClickListener(view -> {
            Intent i = new Intent(CarInfoActivity.this, ChatActivity.class);
            i.putExtra("dealerId", d_id);
            i.putExtra("dealerName", name_d);
            i.putExtra("image", dealer_profile_mage);
            startActivity(i);
        });

        b_remove.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this Car?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cars");
                reference.child(c_id).removeValue().addOnCompleteListener(task -> {
                    Toast.makeText(this, "Car Deleted Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CarInfoActivity.this, DealerHomeActivity.class));
                    finish();
                }).addOnFailureListener(ex -> Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show());

            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = null;
            try {
                dialog = builder.create();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (dialog != null)
                dialog.show();
        });
    }

    private void checkWatchList() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("watchlist").exists()) {
                    FirebaseDatabase.getInstance().getReference().child("watchlist").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            if (snapshot2.child(c_id).exists()) {
                                b_watch_list.setText(R.string.remove_from_watch_list);
                                b_watch_list.setOnClickListener(view -> {
                                    FirebaseDatabase.getInstance().getReference().child("watchlist").child(firebaseUser.getUid()).child(c_id).removeValue();
                                    Toast.makeText(CarInfoActivity.this, "Car removed from watchlist.", Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                b_watch_list.setText(R.string.add_to_watch_list);
                                b_watch_list.setOnClickListener(view -> {
                                    FirebaseDatabase.getInstance().getReference().child("watchlist").child(firebaseUser.getUid()).child(c_id).setValue(true);
                                    Toast.makeText(CarInfoActivity.this, "Car added to watchlist.", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    b_watch_list.setText(R.string.add_to_watch_list);
                    b_watch_list.setOnClickListener(view -> {
                        FirebaseDatabase.getInstance().getReference().child("watchlist").child(firebaseUser.getUid()).child(c_id).setValue(true);
                        Toast.makeText(CarInfoActivity.this, "Car added to watchlist.", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDealerDetails() {
        FirebaseDatabase.getInstance().getReference().child("Dealers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel data = dataSnapshot.getValue(UserModel.class);

                    assert data != null;
                    if (data.getId().equals(d_id)) {
                        s_name.setText(data.getName());
                        name_d = data.getName();
                        dealer_profile_mage = data.getImageUrl();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}