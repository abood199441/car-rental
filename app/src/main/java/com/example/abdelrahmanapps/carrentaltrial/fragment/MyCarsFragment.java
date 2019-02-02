package com.example.abdelrahmanapps.carrentaltrial.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.AddCarActivity;
import com.example.abdelrahmanapps.carrentaltrial.adapter.MyCarsListAdapter;
import com.example.abdelrahmanapps.carrentaltrial.models.MyCar;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyCarsFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {
    ListView carsList;
    Button addCar;
    Button removeCar;

    FirebaseUser user;
    DatabaseReference carListRef;
    ArrayList<MyCar> myCars;
    StorageReference carPictureStorage;
    private static final String TAG = ProfileFragment.class.getSimpleName();


    public MyCarsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        carListRef = FirebaseDatabase.getInstance().getReference("Cars").child(user.getUid());
        View view = inflater.inflate(R.layout.fragment_my_cars, container, false);
        getActivity().setTitle("My Cars");
        getMyCars();
        carsList = view.findViewById(R.id.myCarsList);
        carPictureStorage = FirebaseStorage.getInstance().getReference("Users").child(user.getUid()).child("Cars");
        addCar = view.findViewById(R.id.addCar);
        removeCar = view.findViewById(R.id.removeCar);
        addCar.setOnClickListener(this);

        return view;
    }

    private void getMyCars() {
        myCars = new ArrayList<>();
        carListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myCars = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MyCar myCar = dataSnapshot1.getValue(MyCar.class);
                    myCars.add(myCar);
                }
                updateList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        public void updateList () {
            MyCarsListAdapter adapter = new MyCarsListAdapter(myCars, (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            carsList.setAdapter(adapter);
            carsList.setOnItemClickListener(this);

        }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Choose an action");
        alertBuilder.setItems(new String[]{"Edit Car info", "Remove Car"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        editAcar(position);
                        break;
                    case 1:
                        removeAcar(position);
                        break;
                }
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }



    @Override
        public void onClick (View v){
            switch (v.getId()) {


                case R.id.addCar:
                    Intent addCar = new Intent(getActivity(), AddCarActivity.class);
                    getActivity().startActivity(addCar);
                    getActivity().overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                    break;
                case R.id.removeCar:

                    break;


            }
        }

    private void editAcar(int position){



    }
    private void removeAcar(int position){

        String id = myCars.get(position).getCarID();
        carListRef.child(id).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getMyCars();
            }
        });
        carPictureStorage.child(id).delete();

    }
}



