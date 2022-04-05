package com.example.carreseller.users.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carreseller.R;
import com.example.carreseller.StartActivity;
import com.example.carreseller.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerViewSaved;
//    private PhotoAdapter uploadAdapterSaved;
//    private List<NewsUploads> myPhotoListSaved;


    private RecyclerView recyclerView;
//    private PhotoAdapter photoAdapter;
//    private List<NewsUploads> myPhotoList;

    private CircleImageView imageProfile;
    private TextView fullName;


    private FirebaseAuth auth;

    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();

        imageProfile = view.findViewById(R.id.image_profile);
        ImageView logOut = view.findViewById(R.id.log_out);
        fullName = view.findViewById(R.id.full_name);
        ImageView bookmarked = view.findViewById(R.id.bookmarked);
        ImageView uploaded = view.findViewById(R.id.uploaded);
        Button editProfile = view.findViewById(R.id.edit_profile);

        recyclerView = view.findViewById(R.id.recycle_view_uploaded);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        myPhotoList = new ArrayList<>();
//        photoAdapter = new PhotoAdapter(getContext(), myPhotoList);
//        recyclerView.setAdapter(photoAdapter);

        recyclerViewSaved = view.findViewById(R.id.recycle_view_bookmarked);
        recyclerViewSaved.setHasFixedSize(true);
        recyclerViewSaved.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        myPhotoListSaved = new ArrayList<>();
//        uploadAdapterSaved =new PhotoAdapter(getContext(), myPhotoListSaved);
//        recyclerViewSaved.setAdapter(uploadAdapterSaved);

        profileId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        userInfo();
//        getFollowingCount();
//        myPhotos();
//        getSavedPost();

        editProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfileActivity.class)));

        logOut.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        });

        recyclerViewSaved.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        bookmarked.setOnClickListener(v -> {

            recyclerViewSaved.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        });

        uploaded.setOnClickListener(v -> {

            recyclerViewSaved.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        });

        return view;
    }

    private void userInfo() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
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