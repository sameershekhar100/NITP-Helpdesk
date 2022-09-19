package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ComplainView extends AppCompatActivity {
    TextView name, category, description,location;
    TextView timeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_view);
        Intent intent=getIntent();
        Complain complain=(Complain) intent.getSerializableExtra("item");
        name=findViewById(R.id.title);
        location=findViewById(R.id.location1);
        category=findViewById(R.id.category);
        description=findViewById(R.id.description);
        timeStamp=findViewById(R.id.time);

        name.setText(complain.getName());
        location.setText(complain.getLocation());
        category.setText(complain.getCategory());
        description.setText(complain.getDescription());

        String t=TimeUtils.getTime(Long.parseLong(complain.getTimeStamp()));
        timeStamp.setText(t);

    }
}