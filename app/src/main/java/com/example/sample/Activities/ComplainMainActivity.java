package com.example.sample.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sample.R;
import com.example.sample.fragments.AllComplaintFragment;
import com.example.sample.fragments.MyComplainFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ComplainMainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    Toolbar toolbar;
    ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_main);

        chipNavigationBar=findViewById(R.id.btmbar);
        toolbar=findViewById(R.id.toolbar);
        frameLayout=findViewById(R.id.main_frame);

        setupToolbar();
        setBtmNavBar(savedInstanceState);

    }
    void setupToolbar(){
        toolbar.inflateMenu(R.menu.menu_bar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        return true;
                    case R.id.filter:
                        Toast.makeText(ComplainMainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return false;
            }
        });
    }
    void setBtmNavBar(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            chipNavigationBar.setItemSelected(R.id.my_complaints, true);
            fragmentManager=getSupportFragmentManager();
            MyComplainFragment myComplainFragment=new MyComplainFragment();
            fragmentManager.beginTransaction().replace(R.id.main_frame,myComplainFragment).commit();
        }
        chipNavigationBar.setOnItemSelectedListener(i -> {
            Fragment fragment=null;
            switch (i) {
                case R.id.my_complaints:
                    fragment = new MyComplainFragment();
                    break;
                case R.id.all_complaints:
                    fragment = new AllComplaintFragment();
                    break;
            }
            if(fragment!=null){
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            } else {
                Log.e("TAG", "ERROR");
            }
        });
    }
}