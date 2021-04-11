package com.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Adapters.DashboardAdapter;
import com.fundmanagement.Student.Prior_Request;
import com.fundmanagement.Student.View_Balance;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements DashboardAdapter.MyItemOnClickListener {
    RecyclerView recyclerView;
    String[] str;
    String role;
    ImageView logout;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        role  = getIntent().getStringExtra("role");
        recyclerView = findViewById(R.id.recyclerview);
        logout  = findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        if(role.equals("guide"))
            dummydata();
        else if(role.equals("student"))
            studentdata();
        else if(role.equals("hod"))
            hoddata();
        DashboardAdapter adapter=new DashboardAdapter(str,getApplicationContext(),Dashboard.this);
        recyclerView.setLayoutManager(new GridLayoutManager(Dashboard.this,2));
        recyclerView.setAdapter(adapter);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences =getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                firebaseAuth.signOut();
                Intent it = new Intent(Dashboard.this, Login.class);
                startActivity(it);
                finish();
            }
        });
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

    @Override
    public void OnItemClick(String string) {
        if(role.equals("student")){
            if(string .equals("Prior Request")){
                startActivity(new Intent(Dashboard.this, Prior_Request.class));
            }else if(string.equals("View Balance")){
                startActivity(new Intent(Dashboard.this, View_Balance.class));
            }else if(string.equals("Status")){

            }else if(string.equals("Manage Profile")){
                startActivity(new Intent(Dashboard.this,View.class));
            }
        }
    }
}