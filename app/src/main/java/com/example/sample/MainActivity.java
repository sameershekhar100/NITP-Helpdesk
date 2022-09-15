package com.example.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    Button getOTP, Submit;
    FirebaseAuth  auth = FirebaseAuth.getInstance();
    ProgressBar p;
    Spinner dept, name;
    LinearLayout layout;
    ArrayList<String> dept_array = new ArrayList<>();
    ArrayList<String> name_array = new ArrayList<>();
    String currDept, currName, email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();

        //extract dept and name
        findDept();

        //verify and intent
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verification();
            }
        });

    }

    //set Viewhandlers
    private void getViews() {
        e1 = findViewById(R.id.OTP);
        getOTP = findViewById(R.id.verify);
        Submit = findViewById(R.id.submit);
        dept = findViewById(R.id.department);
        name = findViewById(R.id.name);
        layout = findViewById(R.id.checklayout);
        p = findViewById(R.id.progress);
    }

    //find the dept
    private void findDept() {
        dept_array.add("None");
        dept_array.add("Mechanical");
        dept_array.add("Electrical");
        dept_array.add("Civil");
        dept_array.add("IMSC");
        dept_array.add("Architecture");
        dept_array.add("CSE");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dept_array);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(adapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currDept = dept_array.get(i);
                findName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currDept = dept_array.get(0);
                findName();
            }

        });


    }

    //find name
    private void findName() {
        if (currDept.equals("None")) {
            name_array.clear();
            name.setAdapter(null);
            currDept="";currName="";
            Toast.makeText(this, "Select your Department", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name_array.size() > 1) {

            name_array.clear();

        }

//        Log.v("Taggg", name_array.size()+ "");

        ref = db.collection("Department").document(currDept).collection("names");
        p.setVisibility(View.VISIBLE);
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    name_array.clear();
                    name_array.add("None");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getId().toString();
                        name_array.add(name);
                    }
                    Log.v("Taggg", name_array.get(0)+ "");
                    Toast.makeText(MainActivity.this, "" + name_array.size(), Toast.LENGTH_SHORT).show();
                    setNameSpinner();
                    p.setVisibility(View.GONE);
                }
            }
        });
        // Log.v("Tag....",name_array.size()+"");
    }

    private void setNameSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, name_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);
        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currName = name_array.get(i);
                Log.v("TAGGGGgg", "" + currName);
                Log.v("TAGGGGgg", currDept + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currName = "Select Your Name";
            }

        });
    }

    private void verification() {
        if (currDept.isEmpty() || currDept.equals("None")) {
            Toast.makeText(this, "Select your department", Toast.LENGTH_SHORT).show();
            return;
        } else if (currName == null || currName.equals("None")) {
            Toast.makeText(this, "Select your name", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO Verify...

        getEmail();//email=null
       // while(email==null);
    }

    void getEmail() {
      //  Toast.makeText(this, "dept:" + currDept + " name:" + currName , Toast.LENGTH_SHORT).show();
        ref.document(currName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    email= documentSnapshot.getString("email");
//                    Log.v("gandu mc",email+"");
//                    System.out.println(email+"");
                    signIn();
                }
            }
        });
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            DocumentReference reff=db.collection("Accounts").document(currentUser.getUid());
           reff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if(task.isSuccessful()) {
                       currDept = task.getResult().getString("department");
                       currName = task.getResult().getString("name");
                       Log.v("msg",task.getResult().getId()+"");
                       Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);
                       intent.putExtra("Department", currDept);
                       intent.putExtra("name", currName);
                       startActivity(intent);
                       finish();
                   }
                   else Toast.makeText(MainActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
               }
           });
//            startActivity(new Intent(getApplicationContext(),ComplaintActivity.class));
//            finish();
        }
    }
    void signIn(){

        auth.signInWithEmailAndPassword(email, "abc123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                //    FirebaseUser user=auth.getCurrentUser();
                    Intent intent=new Intent(getApplicationContext(),ComplaintActivity.class);
                    intent.putExtra("Department",currDept);
                    intent.putExtra("name",currName);
                    startActivity(intent);
                    finish();
                }
            }
        });
//        ActionCodeSettings actionCodeSettings =
//                ActionCodeSettings.newBuilder()
//                        // URL you want to redirect back to. The domain (www.example.com) for this
//                        // URL must be whitelisted in the Firebase Console.
//                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
//                        // This must be true
//                        .setHandleCodeInApp(true)
//                        .setAndroidPackageName(
//                                "com.example.sample",
//                                false, /* installIfNotAvailable */
//                                null   /* minimumVersion */)
//                        .build();
//        auth.sendSignInLinkToEmail(email, actionCodeSettings)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("TAG", "Email sent.");
//                        }
//                    }
//                });
    }
}
