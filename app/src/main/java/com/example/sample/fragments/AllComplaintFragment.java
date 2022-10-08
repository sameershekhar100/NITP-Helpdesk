package com.example.sample.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sample.Complain;
import com.example.sample.ComplainAdaptar;
import com.example.sample.ComplainView;
import com.example.sample.ComplaintActivity;
import com.example.sample.ComplaintItemClicked;
import com.example.sample.NewComplaint;
import com.example.sample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AllComplaintFragment extends Fragment implements ComplaintItemClicked {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView textView;
    SwipeRefreshLayout refreshLayout;
    MaterialToolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference complaintRef = db.collection("Complaint");
    FloatingActionButton newComplaint;
    String currDept,currName;
    public AllComplaintFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_complaint, container, false);
        Intent intent=getActivity().getIntent();
        currDept=intent.getStringExtra("Department");
        currName=intent.getStringExtra("name");
        getViews(view);
        Log.v("ppppppp",currName+"");

        setFloatingActionButton();
        setComplaintView();
        setRefreshLayout();

        return view;
    }

    void getViews(View view){
        recyclerView=view.findViewById(R.id.complain_list1);
        newComplaint=view.findViewById(R.id.newComplaint1);
        refreshLayout=view.findViewById(R.id.refresh1);
        textView=view.findViewById(R.id.text1);
    }
    void setFloatingActionButton(){
        newComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getContext(), NewComplaint.class);
                i1.putExtra("Department",currDept);
                i1.putExtra("name",currName);
                startActivity(i1);
                //getActivity().finish();
                // Toast.makeText(ComplaintActivity.this, "complaint added", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setComplaintView() {
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Complain> complaint = new ArrayList<>();


        complaintRef.orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    Log.v("Taggg", task.getResult().size() + "");
                    complaint.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String category = document.getString("category");
                        String name = document.getString("name");
                        String description = document.getString("description");
                        String location = document.getString("location");
                        long number = document.getLong("number");
                        long status = document.getLong("status").intValue();
                        String timestamp = document.getString("timeStamp");
                        Complain complain = new Complain(name, location, description, number, status, category, timestamp);
                        complaint.add(complain);
                    }
                    if (complaint.size() > 0) {
                        ComplainAdaptar complainAdaptar = new ComplainAdaptar(complaint,getActivity(),AllComplaintFragment.this);
                        recyclerView.setAdapter(complainAdaptar);
                    }
                    else{
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }
    private void setRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setComplaintView();
                refreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClicked(Complain item) {
        Intent intent =new Intent(getContext(), ComplainView.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }
}