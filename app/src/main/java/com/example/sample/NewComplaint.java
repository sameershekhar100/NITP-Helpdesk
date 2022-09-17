package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class NewComplaint extends AppCompatActivity {
    EditText name, location, contact, description;
    Button submit;
    RadioGroup type;
    String currDept, currName;
    ArrayList<String>  mycomplaints;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference ref = firestore.collection("Complaint");
    DocumentReference doc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);
        getViews();
        //String mName="";


        Intent intent = getIntent();
        currDept = intent.getStringExtra("Department");
        currName = intent.getStringExtra("name");
        name.setText(currName);
        Log.v("Tag",currDept+"");
        doc=firestore.collection("Department").document(currDept).collection("names").document(currName);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    mycomplaints=(ArrayList<String>)documentSnapshot.get("my_complaint");

                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComplaint();
            }
        });
    }

    private void submitComplaint() {

        String category = getRadioId();
        Toast.makeText(getApplicationContext(), Objects.requireNonNullElse(category, "No answer has been selected"), Toast.LENGTH_SHORT).show();
        String mName = name.getText().toString();
        String mLoc = location.getText().toString();
        String mDes = description.getText().toString();
        long mContact = Long.parseLong(contact.getText().toString());
        long time = System.currentTimeMillis();
        Complain newComplain = new Complain(mName, mLoc, mDes, mContact, 0, category, "" + time);


        ref.add(newComplain).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NewComplaint.this, "Complaint added", Toast.LENGTH_SHORT).show();
                    String id = task.getResult().getId();
                    if(mycomplaints==null) mycomplaints=new ArrayList<String>();
                    mycomplaints.add(id);
                    doc.update("my_complaint",mycomplaints);
                    Intent intent=new Intent(getApplicationContext(),ComplaintActivity.class);
                    intent.putExtra("Department",currDept);
                    intent.putExtra("name",currName);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private String getRadioId() {
        int selectedId = type.getCheckedRadioButtonId();

        if (selectedId == -1) {
            //Toast.makeText(getApplicationContext(),"No answer has been selected", Toast.LENGTH_SHORT).show();
            return null;
        }
        RadioButton radioButton = type.findViewById(selectedId);
        // Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
        return radioButton.getText().toString();
    }


    void getViews() {
        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        contact = findViewById(R.id.contact);
        description = findViewById(R.id.description);
        submit = findViewById(R.id.submit_complaint);
        type = findViewById(R.id.type);

    }
}