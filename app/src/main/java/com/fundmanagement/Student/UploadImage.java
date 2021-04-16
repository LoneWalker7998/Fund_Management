package com.fundmanagement.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fundmanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadImage extends AppCompatActivity {
    String prior_id,name,roll_no,paid_amount,date,category,collectionId;
    ImageView nitc_id_image, bill_image;
    int PICK_IMAGE_REQUEST1 = 71;
    int PICK_IMAGE_REQUEST2 = 72;
    private Uri filePath1,filePath2;
    Button upload_images;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        date = getIntent().getStringExtra("date");
        prior_id = getIntent().getStringExtra("prior_id");
        name = getIntent().getStringExtra("name");
        roll_no = getIntent().getStringExtra("roll_no");
        paid_amount = getIntent().getStringExtra("paid_amount");
        category = getIntent().getStringExtra("category");
        collectionId = getIntent().getStringExtra("collectionId");
        upload_images = findViewById(R.id.upload_images);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        nitc_id_image = findViewById(R.id.nitc_id_image);
        bill_image  = findViewById(R.id.bill_image);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("deleted", Context.MODE_PRIVATE);

        nitc_id_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
            }
        });
        bill_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST2);
            }
        });

        upload_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String image1 = UUID.randomUUID().toString();
                final String image2 = UUID.randomUUID().toString();
                Boolean fileuploaded1 = false;
                Boolean fileuploaded2 = false;

                if(filePath1!=null && filePath2!=null ){
                    final ProgressDialog progressDialog = new ProgressDialog(UploadImage.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    StorageReference reference  = storageReference.child("images/"+image1);
                    reference.putFile(filePath1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadImage.this, "NITC image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadImage.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }else{
                    Toast.makeText(UploadImage.this, "Please select both the images to upload", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(filePath2!=null){
                    final ProgressDialog progressDialog = new ProgressDialog(UploadImage.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    StorageReference reference  = storageReference.child("images/"+image2);
                    reference.putFile(filePath2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadImage.this, "Bill image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadImage.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(UploadImage.this, "Please select NITC Id image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(filePath1!=null && filePath2!=null){
                    CollectionReference collectionReference = firestore.collection("fundRequest");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Map<String,Object> fund =new HashMap<>();
                    fund.put("date", FieldValue.serverTimestamp());
                    fund.put("date1",date);
                    fund.put("name",name);
                    fund.put("status","Pending");
                    fund.put("roll_no",roll_no);
                    fund.put("paid_amount",paid_amount);
                    fund.put("prior_id",prior_id);
                    fund.put("ARR_no","NPY");
                    fund.put("nitc_id",image1);
                    fund.put("bill_image",image2);
                    fund.put("email",user.getEmail().toString());
                    fund.put("category",category);
                    fund.put("message"," ");
                    collectionReference.add(fund).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            DocumentReference del = firestore.collection("prior_request").document(collectionId);
                            del.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("delete", "onSuccess: Data deleted");
                                }
                            });
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("delete","yes");
                            editor.commit();
                            finish();
                            Toast.makeText(UploadImage.this, "Your fund request will be sent to Guide for document verification", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadImage.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode==PICK_IMAGE_REQUEST1 && data!=null && data.getData()!=null){
                filePath1 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    nitc_id_image.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }else if(requestCode==PICK_IMAGE_REQUEST2 && data!=null && data.getData()!=null){
                filePath2 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    bill_image.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}