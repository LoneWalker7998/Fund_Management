package com.fundmanagement.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    String collectionId,uname,uphone,uaddress;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Boolean isdatavalid = false;
    ImageView backbutton;
    Button  update;
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
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        backbutton = findViewById(R.id.toolbar_image);
        update = findViewById(R.id.update);
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
                if(isdatavalid){
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