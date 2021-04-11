package com.fundmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundmanagement.Model.DashboardData;
import com.fundmanagement.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    Context context;
    MyItemOnClickListener listener;
    List itemlist;
    public DashboardAdapter(List itemlist, Context context,MyItemOnClickListener listener) {
        this.itemlist = itemlist;
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
        DashboardData data = (DashboardData) itemlist.get(position);
        holder.main_text.setText(data.getMain_text());
        holder.sub_text.setText(data.getSub_text());
        holder.card_image.setImageResource(data.getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(holder.main_text.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView main_text,sub_text;
        CardView cardView;
        ImageView card_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_text = itemView.findViewById(R.id.main_text);
            sub_text  = itemView.findViewById(R.id.sub_text);
            card_image = itemView.findViewById(R.id.card_image);
            cardView = itemView.findViewById(R.id.cardview_dashboard);
        }
    }

}
