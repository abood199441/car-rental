package com.example.abdelrahmanapps.carrentaltrial.models;


import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserObject {


        public String phoneNumber;
        public String email;
        public String name;

        public UserObject() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public UserObject(String name ,String phoneNumber, String email) {
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.name= name;

        }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("EMAIL", email);
        result.put("NAME", name);
        result.put("NUMBER", phoneNumber);

        return result;
    }





}
