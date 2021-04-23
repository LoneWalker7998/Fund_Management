package com.fundmanagement.Student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundmanagement.Adapters.NotificationAdapter;
import com.fundmanagement.Model.NotificationData;
import com.fundmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {
    ImageView backbutton;
    TextView toolbarText;
    RecyclerView recyclerView;
    List itemlist = new ArrayList();
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        backbutton = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Notifications");
        recyclerView = findViewById(R.id.notification_recycler);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        NotificationAdapter adapter = new NotificationAdapter(itemlist,getApplicationContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(adapter);

        CollectionReference reference = firestore.collection("notification");
        reference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                itemlist.clear();
                if(error!=null){
                    Log.e("notification_data", "onEvent: ",error );
                    return;
                }
                for(QueryDocumentSnapshot document : value){
                    String message = document.getString("message");
                    String email = document.getString("email");
                    if(email.equals(firebaseAuth.getCurrentUser().getEmail())){
                        NotificationData data = new NotificationData(message);
                        itemlist.add(data);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}