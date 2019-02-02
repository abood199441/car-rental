package com.example.abdelrahmanapps.carrentaltrial;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.abdelrahmanapps.carrentaltrial.models.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class UserData {

    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    protected static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageReference = storage.getReference("Users/").child(currentUser.getUid());
    protected static DatabaseReference carDatabaseReference = FirebaseDatabase.getInstance().getReference("Cars").child(currentUser.getUid());
    protected static StorageReference carStorageReference = storageReference.child("Cars/");
    protected static String name;
    protected static String email;
    protected static String phoneNumber;
    protected static SQLiteDatabase localDatabase;

    public static void updateUserData(){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> map = dataSnapshot.getValue(UserObject.class).toMap();
                UserData.name = (String) map.get("NAME");
                UserData.email = (String) map.get("EMAIL");
                UserData.phoneNumber = (String) map.get("NUMBER");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
