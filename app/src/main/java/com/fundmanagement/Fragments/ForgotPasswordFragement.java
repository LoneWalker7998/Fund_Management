package com.fundmanagement.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.CaptivePortal;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fundmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordFragement extends Fragment {

    EditText login_email;
    Button submit;
        FirebaseAuth firebaseAuth;
RelativeLayout relativeLayout;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forgot_password_fragement, container, false);
        login_email = view.findViewById(R.id.login_email);
        submit = view.findViewById(R.id.submit);
        relativeLayout = view.findViewById(R.id.progress_bar);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = this.getActivity().getSharedPreferences("forgot", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isforgot",true);
         editor.apply();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isvalidemail = false;
                if (login_email.getText().toString().trim().isEmpty()) {
                    isvalidemail = false;
                    login_email.setError("Required Field");
                    return;
                } else {
                    isvalidemail = true;
                }
                if (isvalidemail) {
                    String email = login_email.getText().toString().trim();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //relativeLayout.setVisibility(View.GONE);
                            getFragmentManager().popBackStack();
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Reset Password Link Has Been Sent To You Email", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    //relativeLayout.setVisibility(View.GONE);
                }

            }
        });
    }
}
