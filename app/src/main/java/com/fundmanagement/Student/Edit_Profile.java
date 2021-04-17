package com.fundmanagement.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.internal.$Gson$Preconditions;

public class Edit_Profile extends AppCompatActivity {
    EditText name,phone,address,new_password,old_password;
    TextView update;
    String collectionId,uname,uphone,uaddress;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Boolean isdatavalid = false;
    ImageView backbutton;
    TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        collectionId = getIntent().getStringExtra("collectionId");
        uname = getIntent().getStringExtra("name");
        uphone = getIntent().getStringExtra("phone");
        uaddress = getIntent().getStringExtra("address");
        name = findViewById(R.id.edit_name);
        phone = findViewById(R.id.edit_phone);
        address = findViewById(R.id.edit_address);
        new_password = findViewById(R.id.new_password);
        old_password = findViewById(R.id.old_password);
        update = findViewById(R.id.update);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Edit Profile");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name.setText(uname);
        phone.setText(uphone);
        address.setText(uaddress);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData(name);
                validateData(phone);
                validateData(address);
                if(phone.getText().length()!=10){
                    isdatavalid =false;
                    phone.setError("10 Digits only");
                    return;
                }else{
                    isdatavalid = true;
                }
                if(isdatavalid && new_password.getText().toString().isEmpty() && old_password.getText().toString().isEmpty()){
                    DocumentReference ref = firestore.collection("users").document(collectionId);
                    WriteBatch batch = firestore.batch();
                    batch.update(ref,"name",name.getText().toString());
                    batch.update(ref,"phone",phone.getText().toString());
                    batch.update(ref,"address",address.getText().toString());
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Edit_Profile.this, "Profile data updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }else if(isdatavalid && ! new_password.getText().toString().isEmpty() && ! old_password.getText().toString().isEmpty()){
                    DocumentReference ref = firestore.collection("users").document(collectionId);
                    WriteBatch batch = firestore.batch();
                    batch.update(ref,"name",name.getText().toString());
                    batch.update(ref,"phone",phone.getText().toString());
                    batch.update(ref,"address",address.getText().toString());
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Edit_Profile.this, "Profile data updated", Toast.LENGTH_SHORT).show();

                        }
                    });
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    AuthCredential credential  = EmailAuthProvider.getCredential(user.getEmail().toString(),old_password.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(new_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Edit_Profile.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }else{
                                            Toast.makeText(Edit_Profile.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(Edit_Profile.this, "Auth failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void validateData(EditText field) {
        if(field.getText().toString().isEmpty()){
            isdatavalid = false;
            field.setError("Required Field");
        }else
            isdatavalid = true;
    }
}