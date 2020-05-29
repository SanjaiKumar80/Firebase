package com.training.firebase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private EditText emailEditText, passwordEditText;
    private Button mlogin;
    private TextView mText, forgotPassword;
    private ProgressBar progressbar;
    private FirebaseAuth fAuth;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onResume() {
        super.onResume();
        if (amIConnected() == false) {
            ToastGenerate.getInstance(getApplicationContext()).createToastMessage("Check Your Internet Connection",2);
          //  Toast.makeText(this, , Toast.LENGTH_SHORT).show();
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        if (amIConnected() == false) {
            ToastGenerate.getInstance(getApplicationContext()).createToastMessage("Check Your Internet Connection",2);
          //  Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        inital();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private boolean amIConnected() {
        ConnectivityManager contivitymanager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = contivitymanager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void inital() {
        progressbar = findViewById(R.id.prg);
        emailEditText = findViewById(R.id.logemailField);
        passwordEditText = findViewById(R.id.logpasswordField);
        mlogin = findViewById(R.id.LoginBtn);
        mText = findViewById(R.id.NotMember);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        forgotPassword = findViewById(R.id.forgotPassword);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        if (TextUtils.isEmpty(mail)) {

                            Toast.makeText(LoginActivity.this, "Enter Your Email.", Toast.LENGTH_SHORT).show();
                        } else {
                            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });
                passwordResetDialog.create().show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void login() {

        boolean isValid = true;

        if (emailEditText.getText().toString().isEmpty()) {
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
            progressbar.setVisibility(View.VISIBLE);
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressbar.setVisibility(View.GONE);

                        emailEditText.setText("");
                        passwordEditText.setText("");
                        ToastGenerate.getInstance(getApplicationContext()).createToastMessage("Login In Succesully",0);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        progressbar.setVisibility(View.GONE);
                        ToastGenerate.getInstance(getApplicationContext()).createToastMessage("Error",0);
                        //Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } catch (ApiException e) {

            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());

        }

    }

}







