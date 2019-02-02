package com.example.abdelrahmanapps.carrentaltrial;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseMessagingService extends FirebaseMessagingService {




    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
            database.child(user.getUid()).child("token").setValue(s);
        }
    }
}
