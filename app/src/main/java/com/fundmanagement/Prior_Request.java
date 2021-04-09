package com.fundmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Prior_Request extends AppCompatActivity {

    Button btn;
    RecyclerView recyclerView;
    String str[];
    List priorlists = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prior__request);
        btn = findViewById(R.id.add_prior);
        priordata();
        recyclerView = findViewById(R.id.see_prior);
        PriorAdapter adapter = new PriorAdapter(priorlists,Prior_Request.this);
        recyclerView.setLayoutManager(new GridLayoutManager(Prior_Request.this,1));
        recyclerView.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Prior_Request.this,Submit_Prior.class);
                startActivity(it);
            }
        });
    }

    private void priordata(){
        PriorHistory item1 = new PriorHistory("124589","Pending...");
        priorlists.add(item1);

        PriorHistory item2 = new PriorHistory("135689","Approved...");
        priorlists.add(item2);

        PriorHistory item3 = new PriorHistory("1478523","Pending...");
        priorlists.add(item3);
    }
}