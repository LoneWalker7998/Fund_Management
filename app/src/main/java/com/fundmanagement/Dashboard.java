package com.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fundmanagement.Adapters.DashboardAdapter;

public class Dashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    String str[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        String str[] = {
                "Dashboard","Saikat","Dhaval","Abhishek"
        };
        recyclerView = findViewById(R.id.recyclerview);

        DashboardAdapter adapter=new DashboardAdapter(str,Dashboard.this);
        recyclerView.setLayoutManager(new GridLayoutManager(Dashboard.this,2));
        recyclerView.setAdapter(adapter);
    }

}