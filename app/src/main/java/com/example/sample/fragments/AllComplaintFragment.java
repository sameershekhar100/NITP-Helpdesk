package com.example.sample.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sample.Activities.ComplainView;
import com.example.sample.Activities.NewComplaint;
import com.example.sample.Adaptars.ComplainAdaptar;
import com.example.sample.Class.Complain;
import com.example.sample.Interfaces.ComplaintItemClicked;
import com.example.sample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;


public class AllComplaintFragment extends Fragment implements ComplaintItemClicked {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    TextView textView;
    ComplainAdaptar complainAdaptar;
    private FirebaseRemoteConfig remoteConfig;
    SwipeRefreshLayout refreshLayout;
    static DocumentSnapshot lastResult;
    static ArrayList<Complain> complaint = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference complaintRef = db.collection("Complaint");
    FloatingActionButton newComplaint;
    String currDept, currName;
    NestedScrollView nestedScrollView;

    public AllComplaintFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_complaint, container, false);
        Intent intent = getActivity().getIntent();
        currDept = intent.getStringExtra("Department");
        currName = intent.getStringExtra("name");
        getViews(view);
        Log.v("ppppppp", currName + "");
        progressBar.setVisibility(View.GONE);
        setFloatingActionButton();
        setRefreshLayout();
        if (complaint.size() == 0) {
            fetchComplaintView();
        } else {
            complainAdaptar = new ComplainAdaptar(complaint, getContext(), AllComplaintFragment.this);
            recyclerView.setAdapter(complainAdaptar);
        }

        //setComplaintView();
        setupPagination();


        return view;
    }
    void getViews(View view) {
        nestedScrollView = view.findViewById(R.id.nested);
        progressBar = view.findViewById(R.id.progressbar_home);
        recyclerView = view.findViewById(R.id.complain_list1);
        newComplaint = view.findViewById(R.id.newComplaint1);
        refreshLayout = view.findViewById(R.id.refresh1);
        textView = view.findViewById(R.id.text1);
        remoteConfig = FirebaseRemoteConfig.getInstance();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    void setFloatingActionButton() {
        newComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getContext(), NewComplaint.class);
                i1.putExtra("Department", currDept);
                i1.putExtra("name", currName);
                startActivity(i1);
                //getActivity().finish();
                // Toast.makeText(ComplaintActivity.this, "complaint added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPagination() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (lastResult != null) {
                        setComplaintView();
                    }
                    Log.i("HOME_TAG", "last snapshot");
                }
            }
        });
    }



    private void fetchComplaintView() {
        remoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("fetchComplainCount", 3);
        remoteConfig.setDefaultsAsync(defaults);
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            lastResult = null;
                            complaint = new ArrayList<>();
                            setComplaintView();
                        }
                    }
                });
    }

    private void setComplaintView() {
        Query query;
        long c = remoteConfig.getLong("fetchComplainCount");
        if (lastResult == null) {
            query = complaintRef.orderBy("timeStamp", Query.Direction.DESCENDING).limit(c);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            query = complaintRef.orderBy("timeStamp", Query.Direction.DESCENDING).startAfter(lastResult).limit(c);
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.v("Taggg", task.getResult().size() + "");

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Complain complain=document.toObject(Complain.class);
                        complaint.add(complain);
                    }
                    if (lastResult==null && complaint.size()==0) {
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);
                        return;
                    }
                    if (lastResult == null) {
                        complainAdaptar = new ComplainAdaptar(complaint, getActivity(), AllComplaintFragment.this);
                        recyclerView.setAdapter(complainAdaptar);
                    } else
                        complainAdaptar.setData(complaint);
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    int z = task.getResult().getDocuments().size()-1;
                    if (z > 0)
                        lastResult = task.getResult().getDocuments().get(z);
                    else
                        lastResult = null;
                    Log.v("zzzzzzzzz", complaint.size() + "");
                }
            }
        });

    }

    private void setRefreshLayout() {
        complaint.clear();
        lastResult=null;
       // complainAdaptar.setData(complaint);
        refreshLayout.setOnRefreshListener(this::fetchComplaintView);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClicked(Complain item) {
        Intent intent = new Intent(getContext(), ComplainView.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }
}