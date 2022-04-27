package com.example.carreseller.dealer.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreseller.R;
import com.example.carreseller.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private CircleImageView imageProfile;

    private MaterialEditText fullName;
    private MaterialEditText mobileNumber;

    private FirebaseUser fUser;

    private String profileImageUrl;

    private StorageTask uploadTask;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView close = findViewById(R.id.close_d);
        imageProfile = findViewById(R.id.image_profile_d);

        TextView changePhoto = findViewById(R.id.change_photo_d);
        fullName = findViewById(R.id.ep_full_name_d);
        mobileNumber = findViewById(R.id.ep_mobile_number_d);

        Button save = findViewById(R.id.bt_save_d);
        Button cancel = findViewById(R.id.bt_cancel_d);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();

        FirebaseDatabase.getInstance().getReference().child("Dealers").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel users = snapshot.getValue(UserModel.class);

                assert users != null;
                fullName.setText(users.getName());
                mobileNumber.setText(users.getMobile());
                if (users.getImageUrl().equals("default")) {
                    imageProfile.setImageResource(R.drawable.profile_img);
                } else {
                    Picasso.get().load(users.getImageUrl()).into(imageProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        close.setOnClickListener(v -> finish());

        cancel.setOnClickListener(v -> finish());

        changePhoto.setOnClickListener(v -> CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfile.this));

        imageProfile.setOnClickListener(v -> CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfile.this));

        save.setOnClickListener(v -> {
            updateProfile();
        });
    }

    private void updateProfile() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", fullName.getText().toString());
        map.put("phone", mobileNumber.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Dealers").child(fUser.getUid()).updateChildren(map);
        Toast.makeText(this, "Profile Updated.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            assert result != null;

            Uri mImageUri = result.getUri();
            imageProfile.setImageURI(mImageUri);
            uploadProfileImage(mImageUri);

        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage(Uri ImageUri) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        final StorageReference referenceForProfile = storage.getReference().child("Dealers")
                .child(fUser.getUid()).child("profile.jpeg");

        uploadTask = referenceForProfile.putFile(ImageUri);
        uploadTask.addOnCompleteListener(EditProfile.this, (OnCompleteListener<UploadTask.TaskSnapshot>) task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener((OnSuccessListener<UploadTask.TaskSnapshot>) taskSnapshot -> referenceForProfile.getDownloadUrl().addOnSuccessListener(uri -> {
                    profileImageUrl = String.valueOf(uri);
                    updateDatabase(profileImageUrl);
                    pd.dismiss();
                }));
            } else {
                pd.dismiss();
                Toast.makeText(EditProfile.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDatabase(String imageURL) {
        FirebaseDatabase.getInstance().getReference().child("Dealers").child(fUser.getUid()).child("imageUrl").setValue(imageURL);
    }
}