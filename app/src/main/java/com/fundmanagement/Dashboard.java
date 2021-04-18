package com.fundmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundmanagement.Adapters.DashboardAdapter;
import com.fundmanagement.Guide.Document_Verify;
import com.fundmanagement.Guide.Prior_Request_G;
import com.fundmanagement.HOD.ApprovedFundsHod;
import com.fundmanagement.HOD.HOD_Prior;
import com.fundmanagement.HOD.VerifyFundRequest;
import com.fundmanagement.Model.DashboardData;
import com.fundmanagement.Student.Prior_Request;
import com.fundmanagement.Student.ViewStatus;
import com.fundmanagement.Student.View_Profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dashboard extends AppCompatActivity implements DashboardAdapter.MyItemOnClickListener {
    RecyclerView recyclerView;
    String[] str;
    String role;
    ImageView logout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    List itemlist = new ArrayList<>();
    AlertDialog.Builder builder;
    TextView workshop,workshop_balance,seminar,seminar_balance,electronic,electronic_balance,name;
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
        name = findViewById(R.id.username);


        if(role.equals("guide")) {
            workshop.setText("Total Requests");
            electronic.setText("Approved Funds");
            seminar.setText("Pending Request");
            name.setText("Guide");
            DocumentReference ref = firestore.collection("total").document("total_id");
            ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.e("fetch_data", "onEvent: data fetching error in dashboard",error );
                        return;
                    }
                    int total_request = value.getLong("total_request").intValue();
                    int pending_request = value.getLong("pending_request").intValue();
                    int total_fund = value.getLong("total_fund").intValue();
//                        Toast.makeText(Dashboard.this, "Data= "+total_request+" "+pending_request+" "+total_fund, Toast.LENGTH_SHORT).show();
                    workshop_balance.setText(Integer.toString(total_request));
                    seminar_balance.setText(Integer.toString(pending_request));
                    electronic_balance.setText(Integer.toString(total_fund));
                }
            });
            dummydata();
        }
        else if(role.equals("student")) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            DocumentReference reference = firestore.collection("users").document(user.getUid().toString());
            reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.e("fetch_error", "onEvent: Data fetch error in student",error );
                        return;
                    }
                    int workshop_bal = value.getLong("workshop").intValue();
                    int seminar_bal = value.getLong("seminar").intValue();
                    int electronics_bal = value.getLong("electronics").intValue();
                    String uname = value.getString("name").toString();

                    name.setText(uname);
                    workshop_balance.setText(Integer.toString(workshop_bal));
                    seminar_balance.setText(Integer.toString(seminar_bal));
                    electronic_balance.setText(Integer.toString(electronics_bal));
                }
            });
            studentdata();
        }
        else if(role.equals("hod")) {
            name.setText("HOD");
            workshop.setText("Total Requests");
            electronic.setText("Approved Funds");
            seminar.setText("Pending Request");
            DocumentReference ref = firestore.collection("total").document("total_id");
            ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.e("fetch_error", "onEvent: Data fetch error in student",error );
                        return;
                    }
                    int total_request = value.getLong("total_request").intValue();
                    int pending_request = value.getLong("pending_request").intValue();
                    int total_fund = value.getLong("total_fund").intValue();
//                        Toast.makeText(Dashboard.this, "Data= "+total_request+" "+pending_request+" "+total_fund, Toast.LENGTH_SHORT).show();
                    workshop_balance.setText(Integer.toString(total_request));
                    seminar_balance.setText(Integer.toString(pending_request));
                    electronic_balance.setText(Integer.toString(total_fund));
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

                Context context = Dashboard.this;
                builder = new AlertDialog.Builder(context,R.style.CustomDialog);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();

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
            if(string .equals("Prior Request")) {
                startActivity(new Intent(Dashboard.this, Prior_Request.class));
            }else if(string.equals("Status")){
                startActivity(new Intent(Dashboard.this, ViewStatus.class));
            }else if(string.equals("Manage Profile")){
                startActivity(new Intent(Dashboard.this, View_Profile.class));
            }
        }else if(role.equals("guide")){
            if(string.equals("Prior Request")){
                startActivity(new Intent(Dashboard.this, Prior_Request_G.class));
            }else if(string.equals("Document Verification")){
                startActivity(new Intent(Dashboard.this, Document_Verify.class));
            }else if(string.equals("Verified Requests")){
                startActivity(new Intent(Dashboard.this, ApprovedFunds.class));
            }
        }else if(role.equals("hod")){
            if(string.equals("Prior Request")){
                startActivity(new Intent(Dashboard.this, HOD_Prior.class));
            }else  if(string.equals("Fund Request")){
                startActivity(new Intent(Dashboard.this, VerifyFundRequest.class));
            }else  if(string.equals("Request History")){
                startActivity(new Intent(Dashboard.this, ApprovedFundsHod.class));
            }
        }
    }
}