package com.example.abdelrahmanapps.carrentaltrial;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.abdelrahmanapps.carrentaltrial.adapter.CategoryAdapter;
import com.example.abdelrahmanapps.carrentaltrial.adapter.SearchAdapter;
import com.example.abdelrahmanapps.carrentaltrial.models.CarCategoryObject;
import com.example.abdelrahmanapps.carrentaltrial.models.CarListObject;
import com.example.abdelrahmanapps.carrentaltrial.models.MyCar;
import com.example.abdelrahmanapps.carrentaltrial.utils.Constants;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CarCategoryActivity extends AppCompatActivity {

    private static final String TAG = CarCategoryObject.class.getSimpleName();

    private RecyclerView carRecycler;
    List<MyCar> testData;
    String brand;
    String[] optionSelect;
    String seats;
    Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_car_category);
        Bundle extras = getIntent().getExtras();
        brand = extras.getString("BRAND");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle( brand + " Cars");

        Spinner carOptionSpinner = findViewById(R.id.select_car_option);
        optionSelect = getResources().getStringArray(R.array.car_option);
        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, optionSelect);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carOptionSpinner.setAdapter(spinnerArrayAdapter);

        carOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 seats = optionSelect[position];
                 getLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        carRecycler = findViewById(R.id.cars_in_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        carRecycler.setLayoutManager(linearLayoutManager);

    }


    private void getLocation(){
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        if(!(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_PERMISSION_LOCATION);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                currentLocation = task.getResult();
            }
        });
        getData();
    }

    private void getData() {
        testData = new ArrayList<>();
        DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("Cars");
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot users : dataSnapshot.getChildren()){
                    for(DataSnapshot cars : users.getChildren()){
                        MyCar aCar = cars.getValue(MyCar.class);
                        if(aCar.getBrandData().toLowerCase().equals(brand.toLowerCase())) {
                            if (seats != null) {
                                if(aCar.getSeatNumberData().equals(seats)){
                                    testData.add(aCar);
                                }
                            } else {
                                testData.add(aCar);
                            }
                        }
                    }
                }
                // sort the cars from nearest to furthest.
                Collections.sort(testData,comparator);
                updateList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateList(){
        CategoryAdapter mAdapter = new CategoryAdapter(CarCategoryActivity.this, testData);
        carRecycler.setAdapter(mAdapter);

    }


    //distance between 2 cars comparator.
    Comparator<MyCar> comparator = new Comparator<MyCar>() {
        @Override
        public int compare(MyCar o1, MyCar o2) {
            final int R = 6371; // Radius of the earth

            double latDistance1 = Math.toRadians(currentLocation.getLatitude() - o1.getLatitude());
            double lonDistance1 = Math.toRadians(currentLocation.getLongitude() - o1.getLongitude());
            double a1 = Math.sin(latDistance1 / 2) * Math.sin(latDistance1 / 2)
                    + Math.cos(Math.toRadians(currentLocation.getLatitude())) * Math.cos(o1.getLongitude())
                    * Math.sin(lonDistance1 / 2) * Math.sin(lonDistance1 / 2);
            double c1 = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1 - a1));
            double distance1 = R * c1 * 1000; // convert to meters

            double latDistance2 = Math.toRadians(currentLocation.getLatitude() - o2.getLatitude());
            double lonDistance2 = Math.toRadians(currentLocation.getLongitude() - o2.getLongitude());
            double a2 = Math.sin(latDistance2 / 2) * Math.sin(latDistance2 / 2)
                    + Math.cos(Math.toRadians(currentLocation.getLatitude())) * Math.cos(o2.getLongitude())
                    * Math.sin(lonDistance2 / 2) * Math.sin(lonDistance2 / 2);
            double c2 = 2 * Math.atan2(Math.sqrt(a2), Math.sqrt(1 - a2));
            double distance2 = R * c2 * 1000; // convert to meters

            int distance = (int) (distance1-distance2);
            Helper.displayErrorMessage(CarCategoryActivity.this,"" +distance);
            return (int) (distance1-distance2);

        }
    };
}
