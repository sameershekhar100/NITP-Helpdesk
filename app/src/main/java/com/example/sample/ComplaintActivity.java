package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ComplaintActivity extends AppCompatActivity {
RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        getSupportActionBar().hide();

        imageButton=findViewById(R.id.logout);
        imageButton.setOnClickListener(view -> FirebaseAuth.getInstance().signOut());

        setComplaintView();

    }
    void setComplaintView(){
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView=findViewById(R.id.complain_list);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> complaint=new ArrayList<>();
        complaint.add("test");
        complaint.add("test");
        complaint.add("test");
        complaint.add("test");
        complaint.add("test");
        complaint.add("test");
        complaint.add("test");
        complaint.add("test");
        complaint.add("test");
        ComplainAdaptar complainAdaptar=new ComplainAdaptar(complaint);
        recyclerView.setAdapter(complainAdaptar);
    }
}