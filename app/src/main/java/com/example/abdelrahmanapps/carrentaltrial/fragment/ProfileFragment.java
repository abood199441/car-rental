package com.example.abdelrahmanapps.carrentaltrial.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.models.UserObject;
import com.example.abdelrahmanapps.carrentaltrial.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;



public class ProfileFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private TextView name, email, phone, address;
    private ImageView profileImage;


    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getActivity().setTitle("Profile");
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        phone = view.findViewById(R.id.user_phone);
        address = view.findViewById(R.id.user_address);
        name.setText(user.getDisplayName());
        profileImage = view.findViewById(R.id.circleView);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPicture = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                getPicture.setType("image/*");
                getPicture.putExtra("outputX", 256);
                getPicture.putExtra("outputY", 256);
                getPicture.putExtra("crop", "true");
                getActivity().startActivityForResult(getPicture, Constants.REQUEST_IMAGE_LOCAL);
            }
        });
        Uri url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        Picasso.get().load(url).resize(128,128).placeholder(R.drawable.profile).into(profileImage);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                UserObject post = dataSnapshot.getValue(UserObject.class);
                // ...

                Map<String,Object> myMap = post.toMap();

                email.setText((String)myMap.get("EMAIL"));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ref.addListenerForSingleValueEvent(postListener);
        phone.setText(user.getPhoneNumber());


        return view;
    }

}
