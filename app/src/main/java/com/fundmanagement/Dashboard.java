package com.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fundmanagement.Adapters.DashboardAdapter;

public class Dashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    String[] str;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        role  = getIntent().getStringExtra("role");
        recyclerView = findViewById(R.id.recyclerview);
        if(role.equals("guide"))
            dummydata();
        else if(role.equals("student"))
            studentdata();
        else if(role.equals("hod"))
            hoddata();


        DashboardAdapter adapter=new DashboardAdapter(str,Dashboard.this);
        recyclerView.setLayoutManager(new GridLayoutManager(Dashboard.this,2));
        recyclerView.setAdapter(adapter);
    }

    private void hoddata() {
        str = new String[]{"Prior Request", "Fund Request","Request History"};
    }

    private void studentdata() {
        str = new String[]{"Prior Request", "View Balance","Status","Manage Profile"};
    }

    private void dummydata() {
        str = new String[]{"Prior Request", "Document Verification","Verified Documents","Status"};
    }

}