package com.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TermsCondition extends AppCompatActivity {
    TextView toolbarText;
    ImageView backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        toolbarText = findViewById(R.id.toolbar_textview);
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText.setText("Terms & Conditions");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}