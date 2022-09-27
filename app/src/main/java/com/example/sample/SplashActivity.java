package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sample.Admin.AdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    Class c = ComplaintActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        if (Objects.equals(user.getEmail(), "sameershekhar200@gmail.com"))
                            c = AdminActivity.class;
                        startActivity(new Intent(getApplicationContext(), c));

                    } else
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
                , 1000);
    }
}