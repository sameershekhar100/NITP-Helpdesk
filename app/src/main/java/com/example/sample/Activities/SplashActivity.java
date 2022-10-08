package com.example.sample.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sample.Activities.ComplainMainActivity;
import com.example.sample.Activities.MainActivity;
import com.example.sample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    Class c = ComplainMainActivity.class;
    String currDept="",currName="";

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
                        getdetails(user);
                    } else {
                        c = MainActivity.class;
                        startActivity(new Intent(getApplicationContext(),c));
                        finish();
                    }
                }
                , 1000);
    }
    void getdetails(FirebaseUser user){
        Intent intent=new Intent(getApplicationContext(),c);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference reff=db.collection("Accounts").document(user.getUid());
        reff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    currDept = task.getResult().getString("department");
                    currName = task.getResult().getString("name");
                    intent.putExtra("Department",currDept);
                    intent.putExtra("name",currName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}