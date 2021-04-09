package com.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fundmanagement.Student.Prior_Request;

public class Login extends AppCompatActivity {
    Button btn;
    TextView already_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.loginsubmit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Login.this, Dashboard.class);
                it.putExtra("role","hod");
                startActivity(it);
                finish();
            }
        });
        already_account = findViewById(R.id.already_account);
        already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Login.this,SignUp.class);
                startActivity(it);
                finish();
            }
        });
    }
}