package com.training.firebase;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {

    ProgressBar splashProgress;
    int SPLASH_TIME = 3000;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        splashProgress = findViewById(R.id.splashProgress);
        playProgress();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                Intent mySuperIntent = new Intent(ActivitySplash.this, LoginActivity.class);
                startActivity(mySuperIntent);


                finish();

            }
        }, SPLASH_TIME);
    }


    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }


}