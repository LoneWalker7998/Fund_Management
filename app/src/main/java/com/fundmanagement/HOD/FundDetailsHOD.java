package com.fundmanagement.HOD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Guide.FundDetailsGuide;
import com.fundmanagement.R;
import com.fundmanagement.Student.Submit_Prior;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.Write;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.parseColor;

public class FundDetailsHOD extends AppCompatActivity {
    String collectionId;
    TextView arr_no,prior_id,category,date,email,name,paid_amount,roll_number,status,message;
    Button nitc_id,bill;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String image1_url,image2_url;
    ImageView fund_image;
    FirebaseStorage firebaseStorage;
    Button accept,reject;
    AlertDialog.Builder builder;
    ImageView backbutton;
    TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_details_h_o_d);
        collectionId = getIntent().getStringExtra("collectionId");
        arr_no = findViewById(R.id.arr_no_fund);
        prior_id = findViewById(R.id.prior_no_fund);
        category = findViewById(R.id.category_fund);
        date = findViewById(R.id.date_fund);
        email = findViewById(R.id.email_fund);
        name = findViewById(R.id.name_fund);
        paid_amount = findViewById(R.id.paid_amount_fund);
        roll_number = findViewById(R.id.roll_number_fund);
        status = findViewById(R.id.status_fund);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        nitc_id = findViewById(R.id.view_nitc_fund);
        bill = findViewById(R.id.view_bill_fund);
        firebaseStorage = FirebaseStorage.getInstance();
        accept = findViewById(R.id.accept);
        reject  = findViewById(R.id.reject);
        accept.setVisibility(View.VISIBLE);
        reject.setVisibility(View.VISIBLE);
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Fund Details");
        message  =findViewById(R.id.message_fund);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                DocumentReference ref = firestore.collection("fundRequest").document(collectionId);
                Context context = FundDetailsHOD.this;
                builder = new AlertDialog.Builder(context,R.style.CustomDialog);

                builder.setTitle("Accept Request");
                builder.setMessage("Are you sure you want to accept this request");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WriteBatch batch = firestore.batch();
                        batch.update(ref,"status","Approved by HOD");
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                                CollectionReference user_collection = firestore.collection("users");
                                user_collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                            if(documentSnapshot.getString("email").toString().equals(email.getText().toString())){
                                                int value=0;
                                                if(category.getText().toString().equals("Seminar")){
                                                    value =  documentSnapshot.getLong("seminar").intValue();
                                                }else if(category.getText().toString().equals("Workshop")){
                                                    value =  documentSnapshot.getLong("workshop").intValue();
                                                }else if(category.getText().toString().equals("Electronics")){
                                                    value =  documentSnapshot.getLong("electronics").intValue();
                                                }
                                                DocumentReference total = firestore.collection("total").document("total_id");
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
                                                                total_push.put("total_request",total_request);
                                                                total_push.put("pending_request",pending_request);
                                                                total_push.put("total_fund",total_fund+1);
                                                                total.set(total_push).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(FundDetailsHOD.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }else{
                                                            Toast.makeText(FundDetailsHOD.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                DocumentReference change_data = firestore.collection("users").document(documentSnapshot.getId());
                                                String temp_amount = paid_amount.getText().toString();
                                                int amount = value + Integer.parseInt(temp_amount);
                                                WriteBatch batch1 = firestore.batch();
                                                batch1.update(change_data,category.getText().toString().toLowerCase(),amount);
                                                batch1.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(FundDetailsHOD.this, "Balance credited to the student", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                                finish();
                            }
                        });
                        dialogInterface.cancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(parseColor("#2B363C"));
                negativeButton.setTextSize(18);
                Button positvebutton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positvebutton.setTextColor(parseColor("#2B363C"));
                positvebutton.setTextSize(18);
                Button neutralbutton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralbutton.setTextColor(parseColor("#2B363C"));
                neutralbutton.setTextSize(18);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                DocumentReference ref = firestore.collection("fundRequest").document(collectionId);
                Context context = FundDetailsHOD.this;
                builder = new AlertDialog.Builder(context,R.style.CustomDialog);
                builder.setTitle("Rejection Request");
                builder.setMessage("Are you sure you want to reject this request");
                builder.setCancelable(false);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WriteBatch batch = firestore.batch();
                        batch.update(ref,"status","Rejected by HOD");
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        dialogInterface.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(parseColor("#2B363C"));
                negativeButton.setTextSize(18);
                Button positvebutton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positvebutton.setTextColor(parseColor("#2B363C"));
                positvebutton.setTextSize(18);
            }
        });



        DocumentReference reference  = firestore.collection("fundRequest").document(collectionId);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("fetch_data", "onEvent: Error in fetching fund data",error );
                    return;
                }
                arr_no.setText(value.getString("ARR_no").toString());
                prior_id.setText(value.getString("prior_id").toString());
                category.setText(value.getString("category").toString());
                date.setText(value.getString("date1").toString());
                email.setText(value.getString("email").toString());
                name.setText(value.getString("name").toString());
                paid_amount.setText(value.getString("paid_amount").toString());
                roll_number.setText(value.getString("roll_no").toString());
                status.setText(value.getString("status").toString());
                message.setText(value.getString("message"));
                image1_url = value.getString("nitc_id").toString();
                image2_url = value.getString("bill_image").toString();
            }
        });
        nitc_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                builder =new AlertDialog.Builder(FundDetailsHOD.this,R.style.CustomDialog);
                Context context = FundDetailsHOD.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final ImageView fund_image = new ImageView(context);
                fund_image.setMaxHeight(300);
                layout.addView(fund_image);

                StorageReference storageReference = firebaseStorage.getReference().child("images/"+image1_url);
                try {
                    final File localfile  = File.createTempFile(image2_url,"jpg");
                    storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(FundDetailsHOD.this, "Image Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap  = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            fund_image.setImageBitmap(bitmap);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                builder.setView(layout);
                builder.setTitle("image");
                builder.setCancelable(false);
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(parseColor("#2B363C"));
                negativeButton.setTextSize(18);
            }
        });
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                builder =new AlertDialog.Builder(FundDetailsHOD.this,R.style.CustomDialog);
                Context context = FundDetailsHOD.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final ImageView fund_image = new ImageView(context);
                fund_image.setMaxHeight(300);
                layout.addView(fund_image);


                StorageReference storageReference = firebaseStorage.getReference().child("images/"+image2_url);
                try {
                    final File localfile  = File.createTempFile(image2_url,"jpg");
                    storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(FundDetailsHOD.this, "Image Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap  = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            fund_image.setImageBitmap(bitmap);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                builder.setView(layout);
                builder.setTitle("image");
                builder.setCancelable(false);
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(parseColor("#2B363C"));
                negativeButton.setTextSize(18);
            }
        });
    }
}