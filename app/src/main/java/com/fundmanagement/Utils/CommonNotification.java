package com.fundmanagement.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class CommonNotification {
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String str;
    String email;
    Context context;
    public CommonNotification(String str, String email, Context context) {
        this.str = str;
        this.email = email;
        this.context = context;
    }
    public void uploadNotification(){
        firestore = FirebaseFirestore.getInstance();
        CollectionReference reference  = firestore.collection("notification");
        Map<String , Object > details = new HashMap<>();
        details.put("date", FieldValue.serverTimestamp());
        details.put("message",str);
        details.put("email",email);
        reference.add(details).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("notification", "onSuccess: notification uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("notification", "onFailure: ",e);
            }
        });
    }
}
