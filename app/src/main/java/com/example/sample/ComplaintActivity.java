package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sample.Admin.AdminComplainView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class ComplaintActivity extends AppCompatActivity implements ComplaintItemClicked  {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageButton imageButton;
    TextView textView;
    SwipeRefreshLayout refreshLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference complaintRef = db.collection("Complaint");
    FloatingActionButton newComplaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        getSupportActionBar().hide();
        Intent intent=getIntent();
        String currDept=intent.getStringExtra("Department");
        String currName=intent.getStringExtra("name");
        Log.v("ppppppp",currDept+"");

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
        newComplaint=findViewById(R.id.newComplaint);
        newComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getApplicationContext(),NewComplaint.class);
                i1.putExtra("Department",currDept);
                i1.putExtra("name",currName);

                startActivity(i1);
                finish();
               // Toast.makeText(ComplaintActivity.this, "complaint added", Toast.LENGTH_SHORT).show();
            }
        });

        setComplaintView();
        refreshLayout=findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setComplaintView();
                refreshLayout.setRefreshing(false);
                Toast.makeText(ComplaintActivity.this, "refreshed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    void setComplaintView() {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.complain_list);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Complain> complaint = new ArrayList<>();

        complaintRef.orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    Log.v("Taggg", task.getResult().size() + "");
                    complaint.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String category = document.getString("category");
                        String name = document.getString("name");
                        String description = document.getString("description");
                        String location = document.getString("location");
                        long number = document.getLong("number");
                        long status = document.getLong("status").intValue();
                        String timestamp = document.getString("timeStamp");
                        Complain complain = new Complain(name, location, description, number, status, category, timestamp);
                        complaint.add(complain);
                    }
                    if (complaint.size() > 0) {
                        ComplainAdaptar complainAdaptar = new ComplainAdaptar(complaint,getApplicationContext(),ComplaintActivity.this);
                        recyclerView.setAdapter(complainAdaptar);
                    }
                    else{
                        textView=findViewById(R.id.text0);
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onItemClicked(Complain item) {
        Intent intent =new Intent(getApplicationContext(), ComplainView.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }
}