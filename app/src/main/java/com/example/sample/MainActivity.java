package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skydoves.powerspinner.PowerSpinnerView;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    Button getOTP,Submit;
    PowerSpinnerView dept,name;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();

        //extract dept
        findDept();

        //select teacher's name
        findName();

        //verify and intent
        verification();
    }

    private void verification() {
    }

    private void findName() {
    }

    private void getViews(){
        e1=findViewById(R.id.OTP);
        getOTP=findViewById(R.id.verify);
        Submit=findViewById(R.id.submit);
        dept=findViewById(R.id.department);
        name=findViewById(R.id.name);
        layout=findViewById(R.id.checklayout);
    }
    private void findDept(){

    }
}