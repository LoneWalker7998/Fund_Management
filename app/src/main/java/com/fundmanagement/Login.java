package com.fundmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;

import android.annotation.SuppressLint;
import android.app.UiAutomation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Fragments.LoginFragment;
import com.fundmanagement.Fragments.SignUpFragment;
import com.fundmanagement.Student.Prior_Request;
import com.fundmanagement.Utils.Utils;
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

import org.w3c.dom.Document;

public class Login extends AppCompatActivity {

    RadioGroup toggle;
    RadioButton signin,signUp;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle = findViewById(R.id.toggle);
        signin = findViewById(R.id.signin);
        signUp = findViewById(R.id.signUp);
        signUp.setTextColor(R.color.black);
        signin.setTextColor(R.color.white);
        LoginFragment loginFragment = new LoginFragment();
        Utils.setMyFragment(loginFragment, getSupportFragmentManager());

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.signin:
                        signUp.setTextColor(R.color.black);
                        signin.setTextColor(R.color.white);
                        LoginFragment loginFragment = new LoginFragment();
                        loginFragment.setSharedElementEnterTransition(new Utils.DetailsTransition());
                        loginFragment.setEnterTransition(new Fade());
                        loginFragment.setExitTransition(new Fade());
                         loginFragment.setSharedElementReturnTransition(new Utils.DetailsTransition());
                        Utils.setMyFragment(loginFragment, getSupportFragmentManager());
                        break;

                    case R.id.signUp:
                        signin.setTextColor(R.color.black);
                        signUp.setTextColor(R.color.white);
                        SignUpFragment signUpFragment = new SignUpFragment();

                        signUpFragment.setSharedElementEnterTransition(new Utils.DetailsTransition());
                        signUpFragment.setEnterTransition(new Fade());
                        signUpFragment.setExitTransition(new Fade());
                        signUpFragment.setSharedElementReturnTransition(new Utils.DetailsTransition());
                        Utils.setMyFragment(signUpFragment, getSupportFragmentManager());
                        break;


                }


            }
        });
    }
}