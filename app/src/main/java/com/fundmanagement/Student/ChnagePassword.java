package com.fundmanagement.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class ChnagePassword extends AppCompatActivity {
    TextView newpassword,currentpassword;
    Button submit;
    Boolean isdatavalid;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ImageView backbutton;
    TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage_password);
        newpassword = findViewById(R.id.new_password);
        currentpassword = findViewById(R.id.old_password);
        submit = findViewById(R.id.chnage_password_submit);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Chnage Password");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateData(newpassword);
                ValidateData(currentpassword);
                if(isdatavalid){

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    AuthCredential credential  = EmailAuthProvider.getCredential(user.getEmail().toString(),currentpassword.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChnagePassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }else{
                                            Toast.makeText(ChnagePassword.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(ChnagePassword.this, "Auth failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private void ValidateData(TextView newpassword) {
        if(newpassword.getText().toString().isEmpty()){
            isdatavalid=false;
            newpassword.setError("Rquired Field");
            return;
        }else
            isdatavalid=true;
    }
}