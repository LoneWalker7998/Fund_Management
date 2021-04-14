package com.fundmanagement.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fundmanagement.Model.PriorHistoryData;
import com.fundmanagement.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PriorAdapter extends RecyclerView.Adapter<PriorAdapter.ViewHolder> {
    List priorlists;
    Context context_no;

    public PriorAdapter(List priorlists, Context context_no) {
        this.priorlists = priorlists;
        this.context_no = context_no;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context_no).inflate(R.layout.prior_raw,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PriorHistoryData prior1 = (PriorHistoryData) priorlists.get(position);
        holder.cardText1.setText(prior1.getRequestNo());
        holder.cardText2.setText(prior1.getPriorStatus());
        holder.date.setText(prior1.getDate());
        if(prior1.getPriorStatus()=="Pending")
        holder.cardText2.setTextColor(Color.rgb(255,0,0));
    }

    @Override
    public int getItemCount() {
        return priorlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardText1;
        TextView cardText2;
        TextView date;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardText1 = itemView.findViewById(R.id.request_no);
            cardText2 = itemView.findViewById(R.id.prior_status);
            date = itemView.findViewById(R.id.date);
        }
    }

}