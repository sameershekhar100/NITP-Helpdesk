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
import com.example.sample.ComplaintItemClicked;
import com.example.sample.NewComplaint;
import com.example.sample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class MyComplainFragment extends Fragment implements ComplaintItemClicked {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView textView;
    SwipeRefreshLayout refreshLayout;
    ArrayList<Complain> complaint = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference complaintRef = db.collection("Complaint");
    FloatingActionButton newComplaint;
    String currDept,currName;
    ArrayList<String> mycomplainID=new ArrayList<>();
    public MyComplainFragment() {
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

        mycomplainID.clear();
        DocumentReference ref= db.collection("Department").document(currDept).collection("names").document(currName);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    mycomplainID=(ArrayList<String>)doc.get("my_complaint");
                    //Log.v("tt",mycomplainID.size()+"");
                    if(mycomplainID!=null)
                    fetchMyComplaints();
                    else{
                        Log.v("hehe","hehe");
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
    void fetchMyComplaints(){
        complaint.clear();
        ComplainAdaptar complainAdaptar = new ComplainAdaptar(complaint,getActivity(),MyComplainFragment.this);
        recyclerView.setAdapter(complainAdaptar);
//        if (complaint.size() > 0) {
//            recyclerView.setAdapter(complainAdaptar);
//        }
//        else{
//            textView.setVisibility(View.VISIBLE);
//        }
        for(String i: mycomplainID){
            complaintRef.document(i).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Complain complain=documentSnapshot.toObject(Complain.class);
                    complaint.add(complain);
                    complainAdaptar.setData(complaint);
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), "Could not fetch", Toast.LENGTH_SHORT).show());
        }

    }

    @Override
    public void onItemClicked(Complain item) {
        Intent intent =new Intent(getContext(), ComplainView.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }
}