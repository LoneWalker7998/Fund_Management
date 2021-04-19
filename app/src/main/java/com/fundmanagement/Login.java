package com.fundmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
import com.fundmanagement.Fragments.ProgressFragment;
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

public class Login extends AppCompatActivity implements SignUpFragment.onSomeEventListener,LoginFragment.onSomeEventListenerLogin {

    RadioGroup toggle;
    RadioButton signin,signUp;
    SharedPreferences sharedPreferences,sharedPreferences1;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle = findViewById(R.id.toggle);
        signin = findViewById(R.id.signin);
        signUp = findViewById(R.id.signUp);


//
        LoginFragment loginFragment = new LoginFragment();
        Utils.setMyFragment(loginFragment, getSupportFragmentManager());
        sharedPreferences = getSharedPreferences("signup", Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("forgot", Context.MODE_PRIVATE);



        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.signin:
//

                        LoginFragment loginFragment = new LoginFragment();
                        loginFragment.setSharedElementEnterTransition(new Utils.DetailsTransition());
                        loginFragment.setEnterTransition(new Fade());
                        loginFragment.setExitTransition(new Fade());
                         loginFragment.setSharedElementReturnTransition(new Utils.DetailsTransition());
                        Utils.setMyFragment(loginFragment, getSupportFragmentManager());
                        break;

                    case R.id.signUp:
//
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




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    if (sharedPreferences1.getBoolean("isforgot",false)){
         SharedPreferences.Editor editor = sharedPreferences1.edit();
         editor.putBoolean("isforgot",false);

        editor.apply();
        }
    else
        finish();


        }

    @Override
    public void someEvent() {
        signin.setChecked(true);

    }


    @Override
    public void someEventLogin() {
        toggle.setVisibility(View.GONE);
        ProgressFragment progressFragment = new ProgressFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Utils.setMyFragment(progressFragment, fragmentManager);

    }
}