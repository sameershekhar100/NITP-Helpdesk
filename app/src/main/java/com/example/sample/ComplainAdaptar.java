package com.example.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sample.Class.Complain;
import com.example.sample.Class.TimeUtils;

import java.util.ArrayList;

public class ComplainAdaptar extends RecyclerView.Adapter<ComplainAdaptar.ComplaintHolder> {
    ArrayList<Complain> complaint;
    Context context;
    ComplaintItemClicked listener;
    String id;
    public ComplainAdaptar(ArrayList<Complain> complaint, Context context,ComplaintItemClicked listener) {
        this.complaint = complaint;
        this.context = context;
        this.listener=listener;


    }

    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complain_item, parent, false);
        ComplaintHolder holder= new ComplaintHolder(myView);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(complaint.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder holder, int position) {

        Complain complain = complaint.get(position);
        holder.name.setText(complain.getName());
        holder.category.setText(complain.getCategory());
        holder.description.setText(complain.getDescription());
        holder.timeStamp.setText(TimeUtils.getTime(Long.parseLong(complain.getTimeStamp())));
    }
    public void setData(ArrayList<Complain> complaint)
    {
        this.complaint=complaint;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return complaint.size();
    }

    public class ComplaintHolder extends RecyclerView.ViewHolder {
        TextView name, category, description;
        TextView timeStamp;

        public ComplaintHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            description = itemView.findViewById(R.id.description);
            timeStamp = itemView.findViewById(R.id.time);
        }
    }
}

