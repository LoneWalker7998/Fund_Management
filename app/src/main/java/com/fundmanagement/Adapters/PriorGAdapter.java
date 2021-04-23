package com.fundmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fundmanagement.Model.PriorGData;
import com.fundmanagement.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PriorGAdapter extends RecyclerView.Adapter<PriorGAdapter.ViewHolder> {
    List itemlist;
    Context context;
    MyitemOnclick listener;

    public PriorGAdapter(List itemlist, Context context, MyitemOnclick listener) {
        this.itemlist = itemlist;
        this.context = context;
        this.listener = listener;
    }
    public interface  MyitemOnclick{
        void onItemClick(PriorGData data);
    }

    @NonNull
    @Override
    public PriorGAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_prior_g, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriorGAdapter.ViewHolder holder, int position) {
        PriorGData data = (PriorGData) itemlist.get(position);
        holder.status.setText(data.getStatus());
        holder.date.setText(data.getDate());
        holder.email.setText(data.getEmail());
        holder.id.setText(data.getId());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status,email,date,id;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.request_status);
            email = itemView.findViewById(R.id.student_email);
            date = itemView.findViewById(R.id.request_date);
            id = itemView.findViewById(R.id.request_id);
            cardView = itemView.findViewById(R.id.request_cardview);
        }
    }
}
