package com.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Adapters.DashboardAdapter;
import com.fundmanagement.Guide.Prior_Request_G;
import com.fundmanagement.Model.DashboardData;
import com.fundmanagement.Student.Prior_Request;
import com.fundmanagement.Student.View_Balance;
import com.fundmanagement.Student.View_Profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements DashboardAdapter.MyItemOnClickListener {
    RecyclerView recyclerView;
    String[] str;
    String role;
    ImageView logout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    List itemlist = new ArrayList<>();
    TextView workshop,workshop_balance,seminar,seminar_balance,electronic,electronic_balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        role  = getIntent().getStringExtra("role");

        recyclerView = findViewById(R.id.recyclerview);
        logout  = findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        workshop = findViewById(R.id.workshop);
        workshop_balance = findViewById(R.id.workshop_balance);
        seminar = findViewById(R.id.seminar);
        seminar_balance = findViewById(R.id.seminar_balance);
        electronic = findViewById(R.id.electronic);
        electronic_balance = findViewById(R.id.electronic_balance);
        firestore = FirebaseFirestore.getInstance();

        if(role.equals("guide")) {
            workshop.setText("Total Requests");
            electronic.setText("Approved Funds");
            seminar.setText("Pending Request");
            DocumentReference ref = firestore.collection("total").document("total_id");
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot document) {
                    if(document.exists()){
                        int total_request = document.getLong("total_request").intValue();
                        int pending_request = document.getLong("pending_request").intValue();
                        int total_fund = document.getLong("total_fund").intValue();
//                        Toast.makeText(Dashboard.this, "Data= "+total_request+" "+pending_request+" "+total_fund, Toast.LENGTH_SHORT).show();
                        workshop_balance.setText(Integer.toString(total_request));
                        seminar_balance.setText(Integer.toString(pending_request));
                        electronic_balance.setText(Integer.toString(total_fund));
                    }
                }
            });
            dummydata();
        }
        else if(role.equals("student")) {
            studentdata();
        }
        else if(role.equals("hod")) {
            workshop.setText("Total Requests");
            electronic.setText("Approved Funds");
            seminar.setText("Pending Request");
            DocumentReference ref = firestore.collection("total").document("total_id");
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot document) {
                    if(document.exists()){
                        int total_request = document.getLong("total_request").intValue();
                        int pending_request = document.getLong("pending_request").intValue();
                        int total_fund = document.getLong("total_fund").intValue();
//                        Toast.makeText(Dashboard.this, "Data= "+total_request+" "+pending_request+" "+total_fund, Toast.LENGTH_SHORT).show();
                        workshop_balance.setText(Integer.toString(total_request));
                        seminar_balance.setText(Integer.toString(pending_request));
                        electronic_balance.setText(Integer.toString(total_fund));
                    }
                }
            });
            hoddata();
        }
        DashboardAdapter adapter=new DashboardAdapter(itemlist,getApplicationContext(),Dashboard.this);
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

        DashboardData data = new DashboardData("Prior Request","Verify, Approve, Reject Requests",R.drawable.ic_prior_request);
        itemlist.add(data);

        DashboardData data1 = new DashboardData("Fund Request","Verify, Approve, Reject Funds",R.drawable.ic_fund_req);
        itemlist.add(data1);

        DashboardData data2 = new DashboardData("Request History","View Fund History",R.drawable.ic_history);
        itemlist.add(data2);

    }
    private void studentdata() {

        DashboardData data = new DashboardData("Prior Request","Send, View Requests",R.drawable.ic_prior_request);
        itemlist.add(data);

        DashboardData data1 = new DashboardData("Status","View Request Status",R.drawable.ic_status);
        itemlist.add(data1);

        DashboardData data2 = new DashboardData("Manage Profile","View, Update Profile",R.drawable.ic_profile);
        itemlist.add(data2);
    }

    private void dummydata() {
        DashboardData data = new DashboardData("Prior Request","Verify, Approve, Reject Requests",R.drawable.ic_prior_request);
        itemlist.add(data);

        DashboardData data1 = new DashboardData("Document Verification","View, Verify Documents",R.drawable.ic_document_verification);
        itemlist.add(data1);

        DashboardData data2 = new DashboardData("Verified Requests","View Requests",R.drawable.ic_verified);
        itemlist.add(data2);

        DashboardData data3 = new DashboardData("Status","View Request Status",R.drawable.ic_status);
        itemlist.add(data3);

    }

    @Override
    public void OnItemClick(String string) {
        Log.d("string_req", "OnItemClick: "+string);
        if(role.equals("student")){
            if(string .equals("Prior Request")){
                startActivity(new Intent(Dashboard.this, Prior_Request.class));
            }else if(string.equals("View Balance")){
                startActivity(new Intent(Dashboard.this, View_Balance.class));
            }else if(string.equals("Status")){

            }else if(string.equals("Manage Profile")){
                startActivity(new Intent(Dashboard.this, View_Profile.class));
            }
        }else if(role.equals("guide")){
            if(string.equals("Prior Request")){
                startActivity(new Intent(Dashboard.this, Prior_Request_G.class));
            }
        }
    }
}