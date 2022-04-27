package com.example.carreseller.dealer.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carreseller.R;
import com.example.carreseller.StartActivity;
import com.example.carreseller.adapter.CarsAdapter;
import com.example.carreseller.models.CarsModel;
import com.example.carreseller.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarsAdapter carAdapter;
    private List<CarsModel> list;

    private CircleImageView imageProfile;
    private TextView fullName;

    private FirebaseAuth auth;

    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile2, container, false);

        auth = FirebaseAuth.getInstance();

        imageProfile = view.findViewById(R.id.image_profile_d);
        ImageView logOut = view.findViewById(R.id.log_out_d);
        fullName = view.findViewById(R.id.full_name_d);
        ImageView uploaded = view.findViewById(R.id.uploaded_d);
        Button editProfile = view.findViewById(R.id.edit_profile_d);

        recyclerView = view.findViewById(R.id.recycle_view_uploaded_d);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        carAdapter = new CarsAdapter(getContext(), list);
        recyclerView.setAdapter(carAdapter);

        profileId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        dealerInfo();
        
        getUploadedCars();

        editProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfile.class)));

        logOut.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        });

        return view;
    }

    private void getUploadedCars() {
        FirebaseDatabase.getInstance().getReference().child("Cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    CarsModel data = dataSnapshot.getValue(CarsModel.class);

                    assert data != null;
                    if (data.getId() != null) {
                        if (data.getDealerId().equals(profileId)) {
                            list.add(data);
                        }
                    }
                }

                Collections.reverse(list);
                carAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dealerInfo() {

        FirebaseDatabase.getInstance().getReference().child("Dealers").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel users = snapshot.getValue(UserModel.class);

                assert users != null;
                if (users.getImageUrl().equals("default")){
                    imageProfile.setImageResource(R.drawable.profile_img);
                }else {
                    Picasso.get().load(users.getImageUrl()).into(imageProfile);
                }
                fullName.setText(users.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}