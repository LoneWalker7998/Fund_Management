package com.fundmanagement.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.HOD.HOD_Prior;
import com.fundmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Submit_Prior extends AppCompatActivity {
    Button btn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ImageView backbutton;
    TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit__prior);
        btn = findViewById(R.id.send_request);
        firestore = FirebaseFirestore.getInstance();
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Submit Prior Request");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference documentReference = firestore.collection("prior_request");
                DocumentReference total = firestore.collection("total").document("total_id");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Date date = new Date();
                String pattern = "MM/dd/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String strDate = df.format(today);

//                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
//                String strDate = formatter.format(date);


//                Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
//                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
//                String strDate = dt.format(newDate);

                Map<String,Object> prior = new HashMap<>();
                prior.put("student_email",user.getEmail().toString());
                prior.put("prior_id","null");
                prior.put("status","Pending");
                prior.put("date",strDate);
                prior.put("date1", FieldValue.serverTimestamp());
                documentReference.add(prior).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Submit_Prior.this, "Prior Request Sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(Submit_Prior.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

                total.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                int total_request = document.getLong("total_request").intValue();
                                int pending_request = document.getLong("pending_request").intValue();
                                int total_fund = document.getLong("total_fund").intValue();
                                Map<String, Object> total_push = new HashMap<>();
                                total_push.put("total_request",total_request+1);
                                total_push.put("pending_request",pending_request+1);
                                total_push.put("total_fund",total_fund);
                                total.set(total_push).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Submit_Prior.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else{
                            Toast.makeText(Submit_Prior.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}