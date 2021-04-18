package com.fundmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FundDetails extends AppCompatActivity {
    String collectionId;
    TextView arr_no,prior_id,category,date,email,name,paid_amount,roll_number,status;
    Button nitc_id,bill;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String image1_url,image2_url;
    ImageView fund_image;
    FirebaseStorage firebaseStorage;
    ImageView backbutton;
    AlertDialog.Builder builder;
    TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_details);
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
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Fund Details");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                image1_url = value.getString("nitc_id").toString();
                image2_url = value.getString("bill_image").toString();
            }
        });
        nitc_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder =new AlertDialog.Builder(FundDetails.this,R.style.CustomDialog);
                Context context = FundDetails.this;
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
                            Toast.makeText(FundDetails.this, "Image Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap  = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            fund_image.setImageBitmap(bitmap);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                builder.setView(layout);
                builder.setTitle("image");
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder =new AlertDialog.Builder(FundDetails.this,R.style.CustomDialog);
                Context context = FundDetails.this;
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
                            Toast.makeText(FundDetails.this, "Image Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap  = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            fund_image.setImageBitmap(bitmap);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                builder.setView(layout);
                builder.setTitle("image");
                builder.setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}