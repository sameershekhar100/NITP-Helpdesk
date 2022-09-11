package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ComplaintActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageButton imageButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference complaintRef = db.collection("Complaint");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        getSupportActionBar().hide();

        imageButton = findViewById(R.id.logout);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ComplaintActivity.this, "logout", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        setComplaintView();

    }

    void setComplaintView() {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.complain_list);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Complain> complaint = new ArrayList<>();

        complaintRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.v("Taggg", task.getResult().size() + "");
                    complaint.clear();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String category=document.getString("category");
                        String name=document.getString("name");
                        String description=document.getString("description");
                        String location=document.getString("location");
                        long number=document.getLong("contact");
                        long status= document.getLong("status").intValue();
                        String timestamp=document.getString("timestamp");
                        Complain complain=new Complain(name,location,description,number,status,category,timestamp);
                        complaint.add(complain);
                    }
                    ComplainAdaptar complainAdaptar = new ComplainAdaptar(complaint);
                    recyclerView.setAdapter(complainAdaptar);
                }
            }
        });



    }
}