package com.example.carreseller.authentication;

import static android.text.TextUtils.isEmpty;

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
import com.google.firebase.auth.FirebaseAuth;

public class UserLogInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private FirebaseAuth mAuth;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.bt_login);
        TextView tvReg = findViewById(R.id.tv_reg);
        TextView tvAdminLogin = findViewById(R.id.tv_admin_log_in);
        TextView tvDealerLogin = findViewById(R.id.tv_dealer_log_in);

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);

        tvReg.setOnClickListener(v -> startActivity(new Intent(UserLogInActivity.this, UserRegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        tvAdminLogin.setOnClickListener(v -> {
            startActivity(new Intent(UserLogInActivity.this, AdminLogInActivity.class));
            finish();
        });

        tvDealerLogin.setOnClickListener(v -> {
            startActivity(new Intent(UserLogInActivity.this, DealerLogInActivity.class));
            finish();
        });

        btnLogin.setOnClickListener(v -> {
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            if (isEmpty(txt_email)) {
                email.setError("Email Required");
            } else if (isEmpty(txt_password)) {
                password.setError("Password Required");
            } else {
                loginUser(txt_email, txt_password);
            }
        });
    }

    private void loginUser(String email, String password) {

        pd.setMessage("Please Wait!");
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                pd.dismiss();
                Toast.makeText(UserLogInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserLogInActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(UserLogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}