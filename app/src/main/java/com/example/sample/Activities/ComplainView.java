package com.example.sample.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.Class.Complain;
import com.example.sample.Class.TimeUtils;
import com.example.sample.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ComplainView extends AppCompatActivity {
    private TextView name, category, description,location,timeStamp;
     private String[] status_state={"Registered","Ongoing","Completed"};
     private String cId;
    private ImageView status;
    private Spinner statusUpdate;
    private Complain complain;
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
    private int ii;
    private Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_view);
        Intent intent=getIntent();
        complain=(Complain) intent.getSerializableExtra("item");
        findViews();
//        update.setVisibility(View.GONE);
//        statusUpdate.setVisibility(View.GONE);
        String admin= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        assert admin != null;
        if (!admin.equals("sameershekhar200@gmail.com")) {
            return;
        }update.setVisibility(View.VISIBLE);
        statusUpdate.setVisibility(View.VISIBLE);
        setupSpinner();
        setStatus(complain.getStatus());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f();
            }
        });
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
    void setupSpinner(){

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
        Toast.makeText(this, status_state[ii], Toast.LENGTH_SHORT).show();
    }
}