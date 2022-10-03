package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ComplainView extends AppCompatActivity {
    TextView name, category, description,location;
    TextView timeStamp;
    ImageView status;
    Spinner statusUpdate;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String cId;
    Complain complain;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_view);
        Intent intent=getIntent();
        complain=(Complain) intent.getSerializableExtra("item");
        findViews();
        update.setVisibility(View.GONE);
    }
    void findViews(){
        name=findViewById(R.id.title);
        location=findViewById(R.id.location1);
        category=findViewById(R.id.category);
        description=findViewById(R.id.description);
        timeStamp=findViewById(R.id.time);
        status =findViewById(R.id.status);
        name.setText(complain.getName());
        location.setText(complain.getLocation());
        category.setText(complain.getCategory());
        description.setText(complain.getDescription());
        statusUpdate=findViewById(R.id.status_update);
        String t= TimeUtils.getTime(Long.parseLong(complain.getTimeStamp()));
        timeStamp.setText(t);
        update=findViewById(R.id.button_update);
        cId=complain.getTimeStamp();
    }
}