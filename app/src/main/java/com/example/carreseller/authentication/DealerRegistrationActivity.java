package com.example.carreseller.authentication;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreseller.R;
import com.example.carreseller.dealer.DealerHomeActivity;
import com.example.carreseller.users.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DealerRegistrationActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText mobile;
    private EditText password;
    private EditText cPassword;

    private CircleImageView imageProfile;

    private DatabaseReference mRootRef;

    private FirebaseAuth mAuth;


    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_registration);

        name = findViewById(R.id.d_name);
        email = findViewById(R.id.d_email);
        mobile = findViewById(R.id.d_mobile);
        password = findViewById(R.id.d_password);
        cPassword = findViewById(R.id.d_con_password);
        Button btnRegister = findViewById(R.id.d_bt_reg);
        TextView tvLogin = findViewById(R.id.d_tv_login);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(DealerRegistrationActivity.this, DealerLogInActivity.class));
            finish();
        });

        btnRegister.setOnClickListener(v -> {

            String uName = name.getText().toString();
            String uEmail = email.getText().toString();
            String uMobile = mobile.getText().toString();
            String uPassword = password.getText().toString();
            String uCPassword = cPassword.getText().toString();


            if(isEmpty(uName)) {

                name.setError("You must enter your name to register");

            } else if(isEmpty(uEmail)) {

                email.setError("Email is required");

            }else if(isEmpty(uMobile)) {

                email.setError("Mobile Number is required");

            } else if(isEmpty(uPassword)) {

                password.setError("Enter your password.");

            } else if(uPassword.length() < 6) {

                Toast.makeText(DealerRegistrationActivity.this, "Password too short.", Toast.LENGTH_SHORT).show();

            } else if (!uCPassword.equals(uPassword)) {
                Toast.makeText(DealerRegistrationActivity.this, "Password do nat match.", Toast.LENGTH_SHORT).show();
            } else {

                registration(uName, uEmail, uMobile, uPassword);

            }
        });
    }

    private void registration(String uName, String uEmail, String uMobile, String uPassword) {
        pd.setMessage("Please Wait!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(uEmail, uPassword).addOnSuccessListener(authResult -> {

            HashMap<String, Object> map = new HashMap<>();
            map.put("name", uName);
            map.put("email", uEmail);
            map.put("mobile", uMobile);
            map.put("id", mAuth.getCurrentUser().getUid());
            map.put("imageUrl", "default");

            mRootRef.child("Dealers").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task -> {

                if (task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(DealerRegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DealerRegistrationActivity.this, DealerHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            });
        }).addOnFailureListener(e -> {
            System.out.println(e);
            pd.dismiss();
            Toast.makeText(DealerRegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}