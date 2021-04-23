package com.fundmanagement.HOD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Adapters.PriorGAdapter;
import com.fundmanagement.Guide.Prior_Request_G;
import com.fundmanagement.Model.PriorGData;
import com.fundmanagement.R;
import com.fundmanagement.Utils.CommonNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class HOD_Prior extends AppCompatActivity implements PriorGAdapter.MyitemOnclick {
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    List itemlist = new ArrayList();
    AlertDialog.Builder builder;
    ImageView backbutton;
    TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_prior);

        recyclerView = findViewById(R.id.recycler_prior_h);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Prior Request");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refreshData();
    }

    private void refreshData() {
        CollectionReference ref = firestore.collection("prior_request");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String date = document.getString("date");
                        String prior_id = document.getString("prior_id");
                        String status = document.getString("status");
                        String student_email = document.getString("student_email");

                        if(status.equals("Approved by Guide")){
                            PriorGData data = new PriorGData(prior_id, status, date, student_email);
                            data.setCollectionId(document.getId().toString());
                            itemlist.add(data);
                        }
                    }
                    PriorGAdapter adapter = new PriorGAdapter(itemlist,getApplicationContext(),HOD_Prior.this);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(HOD_Prior.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(PriorGData data) {
        DocumentReference ref = firestore.collection("prior_request").document(data.getCollectionId());
        Context context = HOD_Prior.this;
        builder = new AlertDialog.Builder(context,R.style.CustomDialog);
        builder.setTitle("Do you want to accept or reject this request?");
        builder.setMessage("Choose any one of this ");
        builder.setCancelable(false);
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder build;
                build = new AlertDialog.Builder(context,R.style.CustomDialog);
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleBox = new EditText(context);
                titleBox.setHint("Provide Prior Id");
                titleBox.setTextColor(R.color.black);
                layout.addView(titleBox);
                build.setView(layout);
                build.setTitle("Prior Id");
                build.setMessage("Please enter unique Id");
                build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WriteBatch batch = firestore.batch();
                        batch.update(ref,"prior_id",titleBox.getText().toString().trim());
                        batch.update(ref,"status","Approved by HOD");
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                CommonNotification noti = new CommonNotification("Prior request approved by HOD",data.getEmail(),getApplicationContext());
                                noti.uploadNotification();
                                Toast.makeText(HOD_Prior.this, "Prior Id generated", Toast.LENGTH_SHORT).show();
                                itemlist.clear();
                                refreshData();

                                DocumentReference total_ref = firestore.collection("total").document("total_id");
                                total_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            int pending_request = documentSnapshot.getLong("pending_request").intValue();
                                            total_ref.update("pending_request",pending_request-1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                build.show();
            }

        });
        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.update("status","Rejected by HOD").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonNotification noti = new CommonNotification("Prior request rejected by HOD",data.getEmail(),getApplicationContext());
                        noti.uploadNotification();
                        Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                        itemlist.clear();
                        refreshData();
                    }
                });
            }
        });
        builder.show();
    }
}