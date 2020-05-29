package com.training.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullNameEditText, emailEditText, passwordEditText;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private TextView mLoginText;
    private Button signupBtn;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;


    @Override
    protected void onResume() {
        super.onResume();
        if (amIConnected() == false) {
            ToastGenerate.getInstance(getApplicationContext()).createToastMessage("Check Your Internet Connection", 2);
            //   Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (amIConnected() == false) {
            ToastGenerate.getInstance(getApplicationContext()).createToastMessage("Check Your Internet Connection", 2);
            //  Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        initializeWidgets();
        initializeListeners();

    }


    private boolean amIConnected() {
        ConnectivityManager contivitymanager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = contivitymanager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initializeWidgets() {

        inputLayoutName = findViewById(R.id.inputLayoutFullName);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        fullNameEditText = findViewById(R.id.fullnameField);
        emailEditText = findViewById(R.id.emailField);
        passwordEditText = findViewById(R.id.passwordField);
        signupBtn = findViewById(R.id.signUpBtn);
        mLoginText = findViewById(R.id.logtextid);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.idprogress);
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void initializeListeners() {

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void signUp() {

        boolean isValid = true;

        if (fullNameEditText.getText().toString().isEmpty()) {
            inputLayoutName.setError("Your name is mandatory");
            isValid = false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        if (emailEditText.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError("Email is mandatory");
            isValid = false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        if (passwordEditText.getText().toString().trim().length() < 8) {
            inputLayoutPassword.setError(getString(R.string.pwd_validation_msg));
            isValid = false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        if (isValid) {
            progressBar.setVisibility(View.VISIBLE);
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            ToastGenerate.getInstance(getApplicationContext()).createToastMessage(String.valueOf(R.string.signup_success), 1);
            //   Toast.makeText(RegisterActivity.this, R.string.signup_success, Toast.LENGTH_SHORT).show();
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ToastGenerate.getInstance(getApplicationContext()).createToastMessage("User Created", 1);
                        // Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        ToastGenerate.getInstance(getApplicationContext()).createToastMessage("Error", 0);
                        // Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }
}