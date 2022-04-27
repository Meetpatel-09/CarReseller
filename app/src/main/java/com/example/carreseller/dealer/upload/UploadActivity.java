package com.example.carreseller.dealer.upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carreseller.R;
import com.example.carreseller.dealer.DealerHomeActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private ImageView carImageView;

    private Spinner category;

    private EditText carName;
    private EditText company;
    private EditText yearLaunched;
    private EditText seatingCapacity;
    private EditText fuelType;
    private EditText engine;
    private EditText price;

    private String sCarName;
    private String sCompany;
    private String sYearLaunched;
    private String sSeatingCapacity;
    private String sFuelType;
    private String sEngine;
    private String sPrice;
    private String sCategory;

    private FirebaseAuth auth;

    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    private DatabaseReference reference;
    private FirebaseStorage storage;

    private Uri mImageUri;
    String downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();

        pd = new ProgressDialog(this);

        MaterialCardView addImage = findViewById(R.id.add_image_c);
        carImageView = findViewById(R.id.car_image);

        category = findViewById(R.id.car_category);

        carName = findViewById(R.id.car_name);
        company = findViewById(R.id.company_name);
        yearLaunched = findViewById(R.id.year_lauched);
        seatingCapacity = findViewById(R.id.seating_capacity);
        fuelType = findViewById(R.id.fuel_type);
        engine = findViewById(R.id.car_engine);
        price = findViewById(R.id.car_price);

        setSpinner();

        Button uploadCar = findViewById(R.id.upload_car_button);
        Button cancel = findViewById(R.id.cancel_upload);

        addImage.setOnClickListener(v -> CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE).start(UploadActivity.this));

        uploadCar.setOnClickListener(v -> validateData());

        cancel.setOnClickListener(v -> startActivity(new Intent(UploadActivity.this, DealerHomeActivity.class)));

    }

    private void setSpinner() {
        final List<String> catItems = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    catItems.add(dataSnapshot.getKey());
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, catItems);
                category.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void validateData() {

        sCategory = category.getSelectedItem().toString();

        sCarName = carName.getText().toString();
        sCompany = company.getText().toString();
        sYearLaunched = yearLaunched.getText().toString();
        sSeatingCapacity = seatingCapacity.getText().toString();
        sFuelType = fuelType.getText().toString();
        sEngine = engine.getText().toString();
        sPrice = price.getText().toString();

        if (sCarName.isEmpty()) {
            carName.setError("Required");
            carName.requestFocus();
        } else if (sCompany.isEmpty()) {
            company.setError("Required");
            company.requestFocus();
        } else if (sYearLaunched.isEmpty()) {
            yearLaunched.setError("Required");
            yearLaunched.requestFocus();
        } else if (sSeatingCapacity.isEmpty()) {
            seatingCapacity.setError("Required");
            seatingCapacity.requestFocus();
        } else if (sFuelType.isEmpty()) {
            fuelType.setError("Required");
            fuelType.requestFocus();
        } else if (sEngine.isEmpty()) {
            engine.setError("Required");
            engine.requestFocus();
        } else if (sPrice.isEmpty()) {
            price.setError("Required");
            price.requestFocus();
        } else if (sCategory.equals("Select Category")) {
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
        } else if (mImageUri == null) {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        } else {
            uploadBookImage();
        }
    }

    private void uploadBookImage() {
        pd.setMessage("Uploading...");
        pd.show();
        String uniqueString = UUID.randomUUID().toString();

        final StorageReference referenceForProfile = storage.getReference().child("Cars").child(uniqueString+".jpeg");

        uploadTask = referenceForProfile.putFile(mImageUri);
        uploadTask.addOnCompleteListener(UploadActivity.this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> referenceForProfile.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    uploadData();
                }));
            } else {
                pd.dismiss();
                Toast.makeText(UploadActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        DatabaseReference dbRef = reference.child("Cars");
        final String uniqueKey = dbRef.push().getKey();

        HashMap<String, String> map = new HashMap<>();
        map.put("id", uniqueKey);
        map.put("dealerId", Objects.requireNonNull(auth.getCurrentUser()).getUid());
        map.put("carName", sCarName);
        map.put("company", sCompany);
        map.put("launchedYear", sYearLaunched);
        map.put("seatingCapacity", sSeatingCapacity);
        map.put("category", sCategory);
        map.put("fuelType", sFuelType);
        map.put("engine", sEngine);
        map.put("price", sPrice);
        map.put("carImage", downloadUrl);

        assert uniqueKey != null;
        dbRef.child(uniqueKey).setValue(map).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(UploadActivity.this, "Book Uploaded Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this, DealerHomeActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(UploadActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this, DealerHomeActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            assert result != null;

            mImageUri = result.getUri();
            carImageView.setImageURI(mImageUri);
            carImageView.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }
}