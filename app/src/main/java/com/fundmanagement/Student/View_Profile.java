package com.fundmanagement.Student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fundmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class View_Profile extends AppCompatActivity {
    TextView name,email,phone,address,editprofile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__profile);
        name = findViewById(R.id.username_profile);
        email = findViewById(R.id.useremail_profile);
        phone = findViewById(R.id.userphone_profile);
        address = findViewById(R.id.address_profile);
        editprofile = findViewById(R.id.edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user  = firebaseAuth.getCurrentUser();

        DocumentReference reference = firestore.collection("users").document(user.getUid().toString());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null) {
                    Log.e("fetch_data", "onEvent: Error fetching data", error);
                    return;
                }
                name.setText(value.getString("name").toString());
                email.setText(value.getString("email").toString());
                phone.setText(value.getString("phone").toString());
                address.setText(value.getString("address"));
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent(View_Profile.this,Edit_Profile.class);
                it.putExtra("collectionId",user.getUid());
                it.putExtra("name",name.getText().toString());
                it.putExtra("phone",phone.getText().toString());
                it.putExtra("address",address.getText().toString());
                startActivity(it);
            }
        });
    }
}