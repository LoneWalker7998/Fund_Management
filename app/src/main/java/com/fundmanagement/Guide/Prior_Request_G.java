package com.fundmanagement.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fundmanagement.Adapters.PriorGAdapter;
import com.fundmanagement.Model.PriorGData;
import com.fundmanagement.R;
import com.fundmanagement.Student.Prior_Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Prior_Request_G extends AppCompatActivity implements PriorGAdapter.MyitemOnclick {
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    List itemlist = new ArrayList();
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prior__request__g);
        recyclerView = findViewById(R.id.recyclerview_prior_g);
        firebaseAuth  = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        refreshData();

    }

    private void refreshData() {
        CollectionReference reference = firestore.collection("prior_request");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getString("prior_id").toString();
                        String status = document.getString("status").toString();
                        String email = document.getString("student_email").toString();
                        String date = document.getString("date").toString();
                        if(status.equals("Pending")) {
                            PriorGData data = new PriorGData(id, status, date, email);
                            data.setCollectionId(document.getId().toString());
                            itemlist.add(data);
                        }
                    }
                    PriorGAdapter adapter = new PriorGAdapter(itemlist,getApplicationContext(), Prior_Request_G.this);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(Prior_Request_G.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(String str) {
        Context context = Prior_Request_G.this;
        builder = new AlertDialog.Builder(context,R.style.CustomDialog);
        builder.setTitle("Do you want to accept or reject this request?");
        builder.setMessage("Choose any one of this ");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DocumentReference reference = firestore.collection("prior_request").document(str);
                reference.update("status","Approved by Guide").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Request Approved", Toast.LENGTH_SHORT).show();
                        itemlist.clear();
                        refreshData();
                    }
                });
            }
        });
        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DocumentReference reference = firestore.collection("prior_request").document(str);
                reference.update("status","Rejected by Guide").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                        itemlist.clear();
                        refreshData();
                    }
                }) ;
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


}