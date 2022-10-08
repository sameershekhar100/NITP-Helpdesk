package com.example.sample.Activities;

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

import com.example.sample.Class.Complain;
import com.example.sample.R;
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
        Intent intent = getIntent();
        currDept = intent.getStringExtra("Department");
        currName = intent.getStringExtra("name");
        name.setText(currName);
        Log.v("Tag",currDept+"");
        doc=firestore.collection("Department").document(currDept).collection("names").document(currName);


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


        ref.document(newComplain.getTimeStamp()).set(newComplain).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NewComplaint.this, "Complaint added", Toast.LENGTH_SHORT).show();
                String id = newComplain.getTimeStamp();
                doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            mycomplaints=(ArrayList<String>)documentSnapshot.get("my_complaint");
                            if(mycomplaints==null) mycomplaints=new ArrayList<String>();
                            mycomplaints.add(id);
                            doc.update("my_complaint",mycomplaints);
                            Toast.makeText(NewComplaint.this, "Complaint added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent=new Intent(getApplicationContext(), ComplainMainActivity.class);
                intent.putExtra("Department",currDept);
                intent.putExtra("name",currName);
                startActivity(intent);
                finish();
            }
        });


    }

    private String getRadioId() {
        int selectedId = type.getCheckedRadioButtonId();

        if (selectedId == -1) {
            return null;
        }
        RadioButton radioButton = type.findViewById(selectedId);
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