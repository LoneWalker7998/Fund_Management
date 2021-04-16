package com.fundmanagement.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Model.PriorHistoryData;
import com.fundmanagement.Adapters.PriorAdapter;
import com.fundmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Prior_Request extends AppCompatActivity implements PriorAdapter.MyitemClickOnPrior{

    Button btn;
    RecyclerView recyclerView;
    String str[];
    TextView toolbar_text;
    ImageView toolbar_image;
    List priorlists = new ArrayList();
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prior__request);
        btn = findViewById(R.id.add_prior);
        recyclerView = findViewById(R.id.see_prior);
        toolbar_text = findViewById(R.id.toolbar_textview);
        toolbar_image = findViewById(R.id.toolbar_image);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        toolbar_text.setText("Prior Request");

        CollectionReference collectionReference = firestore.collection("prior_request");
        collectionReference.orderBy("date1", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("order error", "onEvent: Listen Failed ",error );
                    return;
                }
                if(!value.isEmpty()) {
                    priorlists.clear();
                    for (QueryDocumentSnapshot document : value) {
                        FirebaseUser user  = firebaseAuth.getCurrentUser();
                        if(user.getEmail().toString().equals(document.getString("student_email"))) {
                            String prior_id = document.getString("prior_id").toString();
                            String status = document.getString("status").toString();
                            String date  = document.getString("date");
                            PriorHistoryData item1 = new PriorHistoryData(prior_id, status,date);
                            item1.setId(document.getId());
                            priorlists.add(item1);
                        }
                    }
                    PriorAdapter adapter = new PriorAdapter(priorlists,getApplicationContext(),Prior_Request.this);
                    recyclerView.setLayoutManager(new GridLayoutManager(Prior_Request.this,1));
                    recyclerView.setAdapter(adapter);
                }
            }
        });


        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Prior_Request.this, Submit_Prior.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void OnItemClick(PriorHistoryData data) {
        if(data.getPriorStatus().equals("Approved by HOD")){
            Intent it = new Intent(Prior_Request.this,Add_Bill.class);
            it.putExtra("id",data.getRequestNo());
            it.putExtra("collectionId",data.getId());
            startActivity(it);
        }
    }

}