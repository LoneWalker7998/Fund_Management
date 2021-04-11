package com.fundmanagement.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Dashboard;
import com.fundmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {
    Button btn, signup,forgot_password,resend_btn;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder builder;
    TextView login_email,login_password;
    Boolean isValidData= false;
    FirebaseFirestore firestore;
    SharedPreferences sharedPreferences;
    RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btn=view.findViewById(R.id.loginsubmit);
        forgot_password = view.findViewById(R.id.forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();
        resend_btn = view.findViewById(R.id.verify_email);
        firestore = FirebaseFirestore.getInstance();
        relativeLayout = view.findViewById(R.id.progress_bar);
        sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("username")){
            Log.d("sharedpref", "onCreate: came into sharedpref");
            String role = sharedPreferences.getString("role","");
            Log.d("sharedpref", "onCreate: role = "+role);
            if(role.equals("hod")){
                Intent it = new Intent(getContext(), Dashboard.class);
                it.putExtra("role", "hod");
                startActivity(it);

            }else if(role.equals("student")){
                Intent it = new Intent(getContext(), Dashboard.class);
                it.putExtra("role", "student");
                startActivity(it);

            }
            else if(role.equals("guide")){
                Intent it = new Intent(getContext(), Dashboard.class);
                it.putExtra("role", "guide");
                startActivity(it);

            }
        }else{
            Log.d("sharedpref", "onCreate: NO shared pref");
        }
        signup = view.findViewById(R.id.singuppage);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Context context = getContext();
                builder = new AlertDialog.Builder(context,R.style.CustomDialog);
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleBox = new EditText(context);
                titleBox.setHint("Enter Email Id");
                titleBox.setTextColor(R.color.black);
                layout.addView(titleBox);
                builder.setView(layout);
                builder.setTitle("Forgot Password");
                builder.setMessage("Enter Correct Email Id");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        boolean isvalidemail = false;
                        if(titleBox.getText().toString().trim().isEmpty()){
                            isvalidemail=false;
                            titleBox.setError("Required Field");
                            return;
                        }else{
                            isvalidemail = true;
                        }
                        if(isvalidemail){
                            String email = titleBox.getText().toString().trim();
                            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    relativeLayout.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Reset Password Link Has Been Sent To You Email", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(context, "Error "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                            dialogInterface.cancel();
                            relativeLayout.setVisibility(View.GONE);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_email = view.findViewById(R.id.login_email);
                login_password  = view.findViewById(R.id.login_password);
                String mail = login_email.getText().toString().trim();
                String pass = login_password.getText().toString().trim();
                isdatavalid(login_email);
                isdatavalid(login_password);
                if(pass.length()<8){
                    isValidData = false;
                    login_password.setError("Must be 8 characters");
                    return;
                }else{
                    isValidData = true;
                }
                if(isValidData){
                    relativeLayout.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                                String id = firebaseUser.getUid();
                                DocumentReference documentReference = firestore.collection("users").document(id);
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String role = documentSnapshot.getString("role");
                                        if(role.equals("guide")){
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username",firebaseUser.getEmail());
                                            editor.putString("role",role);
                                            editor.commit();
                                            Intent it = new Intent(getContext(),Dashboard.class);
                                            it.putExtra("role",role);
                                            startActivity(it);

                                        }else if(role.equals("hod")){
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username",firebaseUser.getEmail());
                                            editor.putString("role",role);
                                            editor.commit();
                                            Intent it = new Intent(getContext(),Dashboard.class);
                                            it.putExtra("role",role);
                                            startActivity(it);

                                        }else if(role.equals("student")){
                                            if(firebaseUser.isEmailVerified()){
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("username",firebaseUser.getEmail());
                                                editor.putString("role",role);
                                                editor.commit();
                                                Intent it = new Intent(getContext(),Dashboard.class);
                                                it.putExtra("role",role);
                                                startActivity(it);

                                                relativeLayout.setVisibility(View.GONE);
                                            }else{
                                                relativeLayout.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Verify Your Email First", Toast.LENGTH_SHORT).show();
                                                resend_btn.setVisibility(View.VISIBLE);
                                                resend_btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        relativeLayout.setVisibility(View.VISIBLE);
                                                        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                relativeLayout.setVisibility(View.GONE);
                                                                Toast.makeText(getContext(), "Email has been sent to your account", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                relativeLayout.setVisibility(View.GONE);
                                                                Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        relativeLayout.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                relativeLayout.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(getContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                relativeLayout.setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            relativeLayout.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        return view;
    }
    private void isdatavalid(TextView field) {
        if(field.getText().toString().trim().isEmpty()){
            isValidData = false;
            field.setError("Required Field");
            return;
        }else
            isValidData=true;
    }
}