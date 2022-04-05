package com.example.carreseller.authentication;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreseller.R;
import com.example.carreseller.users.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class UserRegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText mobile;
    private EditText password;
    private EditText cPassword;

    private DatabaseReference mRootRef;

    private FirebaseAuth mAuth;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.con_password);
        Button btnRegister = findViewById(R.id.bt_reg);
        TextView tvLogin = findViewById(R.id.tv_login);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(UserRegisterActivity.this, UserLogInActivity.class));
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

                Toast.makeText(UserRegisterActivity.this, "Password too short.", Toast.LENGTH_SHORT).show();

            } else if (!uCPassword.equals(uPassword)) {
                Toast.makeText(UserRegisterActivity.this, "Password do nat match.", Toast.LENGTH_SHORT).show();
            } else {

                registration(uName, uEmail, uMobile, uPassword);

            }
        });
    }

    private void registration(final String name, final String email, final String mobile, final String password) {

        pd.setMessage("Please Wait!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("mobile", mobile);
                map.put("id", mAuth.getCurrentUser().getUid());
                map.put("imageUrl", "default");

                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(UserRegisterActivity.this, "Update your profile for better experience.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserRegisterActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
                pd.dismiss();
                Toast.makeText(UserRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}