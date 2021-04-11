package com.fundmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fundmanagement.R;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    String str[];
    Context context;
    MyItemOnClickListener listener;
    public DashboardAdapter(String[] str, Context context,MyItemOnClickListener listener) {
        this.str = str;
        this.context = context;
        this.listener = listener;
    }
    public interface  MyItemOnClickListener{
        void OnItemClick(String string);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.dashboard_raw,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardText.setText(str[position]);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(str[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return str.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardText;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardText = itemView.findViewById(R.id.cardtext );
            cardView = itemView.findViewById(R.id.cardview_dashboard);
        }
    }

}
