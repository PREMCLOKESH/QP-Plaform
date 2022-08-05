package com.nmamit.nmamitqp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.snackbar.Snackbar;

public class SplashScreenActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(CheckNetwork.isInternetAvailable(SplashScreenActivity.this)) //returns true if internet available
                {

                    Intent i = new Intent(SplashScreenActivity.this, login.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, R.string.text_label,Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();                }
            }
        }, 3000);
    }
}