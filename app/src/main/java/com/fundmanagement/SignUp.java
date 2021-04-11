package com.fundmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText email,name,phone,address,password,confirm_password;
    Button login,signup,forgot_password;
    Boolean isDataValid = false;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.singup_button);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignUp.this,Login.class);
                startActivity(it);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData(email);
                validateData(name);
                validateData(phone);
                validateData(address);
                validateData(password);
                validateData(confirm_password);
                if(email.getText().toString().trim().contains("nitc.ac.in")){
                    isDataValid = true;
                }else{
                    isDataValid = false;
                    email.setError("Use NITC Id Only");
                    return;
                }

                if(!((phone.getText().toString().trim().length()) == 10)){
                    isDataValid = false;
                    phone.setError("Only 10 Digits Allowed");
                    return;
                }else{
                    isDataValid=true;
                }

                if(password.getText().toString().trim().length()<8){
                    isDataValid = false;
                    password.setError("Use At least 8 Characters");
                    return;
                }else{
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        isDataValid =true;
                    }else{
                        isDataValid = false;
                        confirm_password.setError("Password Doesn't match");
                        return;
                    }
                }
                if(isDataValid){
                    Toast.makeText(SignUp.this, "Data is verified", Toast.LENGTH_SHORT).show();
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUp.this, "Verify Your Email", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                String userId = firebaseUser.getUid();
                                DocumentReference documentReference = firestore.collection("users").document(userId);

                                int index_of_at = email.getText().toString().trim().indexOf('@');
                                String roll_no = email.getText().toString().trim().substring(index_of_at-9,index_of_at);
                                Map<String,Object> user = new HashMap<>();
                                user.put("name",name.getText().toString().trim());
                                user.put("role","student");
                                user.put("email",email.getText().toString().trim());
                                user.put("phone",phone.getText().toString().trim());
                                user.put("address",address.getText().toString().trim());
                                user.put("rollno",roll_no);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUp.this, "User Data Created", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUp.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent it = new Intent(SignUp.this,Login.class);
                                startActivity(it);
                                finish();
                            }else{
                                Toast.makeText(SignUp.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void validateData(EditText field) {
        if(field.getText().toString().isEmpty()){
            isDataValid =false;
            field.setError("Required Field");
            return;
        }else{
            isDataValid = true;
        }
    }
}