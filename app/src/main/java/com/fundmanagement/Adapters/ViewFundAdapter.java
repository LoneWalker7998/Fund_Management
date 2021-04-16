package com.fundmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundmanagement.Model.FundRequestData;
import com.fundmanagement.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewFundAdapter extends RecyclerView.Adapter<ViewFundAdapter.ViewHolder> {
    List itemlist;
    Context context;
    MyItemOnFundListener listener;

    public ViewFundAdapter(List itemlist, Context context, MyItemOnFundListener listener) {
        this.itemlist = itemlist;
        this.context = context;
        this.listener = listener;
    }

    public interface  MyItemOnFundListener{
        void OnItemClick(String str);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.verify_raw,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FundRequestData data = (FundRequestData) itemlist.get(position);
        holder.status.setText(data.getStatus());
        holder.name.setText(data.getName());
        holder.arr_no.setText(data.getARR_no());
        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(data.getCollectionId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status,arr_no,name;
        ImageView next;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status_raw);
            name = itemView.findViewById(R.id.name_raw);
            arr_no = itemView.findViewById(R.id.arr_no_raw);
            next = itemView.findViewById(R.id.go_next_raw);
        }
    }
}
