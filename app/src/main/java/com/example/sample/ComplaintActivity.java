package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ComplaintActivity extends AppCompatActivity {
RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        getSupportActionBar().hide();
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