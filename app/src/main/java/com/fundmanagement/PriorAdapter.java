package com.fundmanagement;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.fundmanagement.R.color.purple_700;

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
        PriorHistory prior1 = (PriorHistory) priorlists.get(position);
        holder.cardText1.setText(prior1.getRequestNo());
        holder.cardText2.setText(prior1.getPriorStatus());
        if(prior1.getPriorStatus()=="Pending...")
        holder.cardText2.setTextColor(Color.rgb(255,0,0));
    }

    @Override
    public int getItemCount() {
        return priorlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardText1;
        TextView cardText2;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardText1 = itemView.findViewById(R.id.request_no);
            cardText2 = itemView.findViewById(R.id.prior_status);
        }
    }

}