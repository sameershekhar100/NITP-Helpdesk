package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ComplainView extends AppCompatActivity {
    TextView name, category, description,location;
    TextView timeStamp;
    ImageView status;
    Spinner statusUpdate;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String cId;
    DocumentReference doc;
    Complain complain;
    Button update;
    int ii;
    ArrayList<String> arr=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_view);
        Intent intent=getIntent();
        complain=(Complain) intent.getSerializableExtra("item");
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
        String t=TimeUtils.getTime(Long.parseLong(complain.getTimeStamp()));
        timeStamp.setText(t);
        update=findViewById(R.id.button_update);
        cId=complain.getTimeStamp();

        setupSpinner();
        setStatus(complain.getStatus());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f();
            }
        });

        arr.add("R.drawable.circle1");
        arr.add("R.drawable.circle2");
        arr.add("R.drawable.circle3");
        //String s=getResources().getResourceName(status.getId());




    }
    void setupSpinner(){
        String[] status_state={"Registered","Ongoing","Completed"};
        ArrayAdapter<String> adapter =new  ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item,status_state);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusUpdate.setAdapter(adapter);
        statusUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ii = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    void setStatus(long status0){

        if(status0==2){
            status.setImageResource(R.drawable.circle1);
        }
        else if(status0==1){
            status.setImageResource(R.drawable.circle2);
        }
        else{
            status.setImageResource(R.drawable.circle3);
        }
    }
    void f(){
        DocumentReference doc=db.collection("Complaint").document(complain.getTimeStamp());
        doc.update("status",ii);
        setStatus(ii);
    }

}