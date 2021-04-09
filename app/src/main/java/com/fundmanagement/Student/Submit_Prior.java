package com.fundmanagement.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fundmanagement.HOD.HOD_Prior;
import com.fundmanagement.R;

public class Submit_Prior extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit__prior);

        btn = findViewById(R.id.send_request);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Submit_Prior.this, HOD_Prior.class);
                startActivity(it);
            }
        });
    }
}