package com.fundmanagement.Student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fundmanagement.Adapters.ViewFundAdapter;
import com.fundmanagement.FundDetails;
import com.fundmanagement.Model.FundRequestData;
import com.fundmanagement.R;
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

public class ViewStatus extends AppCompatActivity implements ViewFundAdapter.MyItemOnFundListener {
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    List itemlist = new ArrayList();
    ImageView backbutton;
    TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status);
        recyclerView = findViewById(R.id.recyclerview_status);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        CollectionReference reference = firestore.collection("fundRequest");
        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Status");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                itemlist.clear();
                if(error!=null ){
                    Log.e("fatch_error", "onEvent: Error fetching data",error );
                    return;
                }
                for(QueryDocumentSnapshot document : value){
                    if(document.getString("email").equals(user.getEmail().toString())) {
                        String arr_no = document.getString("ARR_no").toString();
                        String status = document.getString("status").toString();
                        String name = document.getString("name").toString();
                        FundRequestData data = new FundRequestData(arr_no, name, status);
                        data.setCollectionId(document.getId());
                        itemlist.add(data);
                    }
                }
                ViewFundAdapter adapter = new ViewFundAdapter(itemlist,getApplicationContext(),ViewStatus.this);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void OnItemClick(String str) {
//        Toast.makeText(this, ""+str, Toast.LENGTH_SHORT).show();
        Intent it = new Intent(ViewStatus.this,FundDetails.class);
        it.putExtra("collectionId",str);
        startActivity(it);
    }
}