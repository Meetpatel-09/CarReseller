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
import com.example.carreseller.dealer.DealerHomeActivity;
import com.example.carreseller.users.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DealerLogInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private FirebaseAuth mAuth;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_log_in);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView tvReg = findViewById(R.id.tv_reg_d);
        email = findViewById(R.id.email_d);
        password = findViewById(R.id.password_d);
        Button btnLogin = findViewById(R.id.bt_login_d);

        tvReg.setOnClickListener(v -> startActivity(new Intent(DealerLogInActivity.this, DealerRegistrationActivity.class)));

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);

        btnLogin.setOnClickListener(v -> {
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            if (isEmpty(txt_email)) {
                email.setError("Email Required");
            } else if (isEmpty(txt_password)) {
                password.setError("Password Required");
            } else {
                loginDealer(txt_email, txt_password);
            }
        });
    }

    private void loginDealer(String txt_email, String txt_password) {
        pd.setMessage("Please Wait!");
        pd.show();

        mAuth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                pd.dismiss();
                Toast.makeText(DealerLogInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DealerLogInActivity.this, DealerHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(DealerLogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}