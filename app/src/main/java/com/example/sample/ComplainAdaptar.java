package com.example.sample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ComplainAdaptar extends RecyclerView.Adapter<ComplainAdaptar.ComplaintHolder>  {
    ArrayList<String> complaint;
    public ComplainAdaptar(ArrayList<String> complaint){
        this.complaint=complaint;
    }
    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.complain_item,parent,false);
        return new ComplaintHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder holder, int position) {
        String s=complaint.get(position);
        holder.textView.setText(s);
    }
    @Override
    public int getItemCount() {
        return complaint.size();
    }

    public class ComplaintHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ComplaintHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.title);
        }
    }
}
