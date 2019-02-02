package com.example.abdelrahmanapps.carrentaltrial.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.adapter.SearchAdapter;
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


public class SearchCarFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SearchCarFragment.class.getSimpleName();
    private LinearLayout filterLayout;
    private LinearLayout sortLayout;
    private RecyclerView carRecyclerView;
    List<MyCar> data;
    private List<MyCar> filteredSortedList;

    private int selectedPriceAmount;
    Location currentLocation;

    String[] carSizes, ratingOptions;
    String selectedSize, selectedRating;
    public SearchCarFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ratingOptions = getResources().getStringArray(R.array.rating);
        carSizes = getResources().getStringArray(R.array.car_size);
        selectedPriceAmount = 200;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_car, container, false);

        getActivity().setTitle("Explore Cars");
        filterLayout =  view.findViewById(R.id.filter_cars);
        filterLayout.setOnClickListener(this);

        sortLayout = view.findViewById(R.id.sort_cars);
        sortLayout.setOnClickListener(this);

        carRecyclerView = view.findViewById(R.id.all_cars);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        carRecyclerView.setLayoutManager(linearLayoutManager);
        carRecyclerView.setHasFixedSize(true);

        sortListByLocation();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sort_cars:
                openSortDialog();
                break;
            case R.id.filter_cars:
                openFilterDialog();
                break;
        }
    }


    private void sortListByLocation(){
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(getContext());
        if(!(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_PERMISSION_LOCATION);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                currentLocation = task.getResult();
                getCarsData();
            }
        });
    }

    private void getCarsData() {
        data = new ArrayList<>();
        DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("Cars");
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    for(DataSnapshot car : user.getChildren()){
                       MyCar aCar = car.getValue(MyCar.class);
                        if(Integer.parseInt(aCar.getPrice())<=selectedPriceAmount) {
                            if (selectedSize != null) {
                                if(aCar.getSize().equals(selectedSize)){
                                    data.add(aCar);
                                    Helper.displayErrorMessage(getContext(),"adding a car");
                                }
                            } else {
                                data.add(aCar);
                            }
                        }
                    }
                }
                if(currentLocation!=null) {
                    Collections.sort(data, comparator);
                }
                updateList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateList(){
        SearchAdapter adapter = new SearchAdapter(getContext(),data);
        carRecyclerView.setAdapter(adapter);

    }


    private void openSortDialog(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View subView = inflater.inflate(R.layout.sort_layout, null);

        SeekBar seekBar = subView.findViewById(R.id.seek_bar_price);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedPriceAmount = progress;
                TextView currentProgress = subView.findViewById(R.id.sortText);
                currentProgress.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SORT CAR BY Hour PRICE");
        builder.setView(subView);

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectedPriceAmount == 0){
                    Helper.displayErrorMessage(getActivity(), "Nothing is selected");
                }else{
                    Helper.displayErrorMessage(getActivity(), "Price " + selectedPriceAmount);
                    getCarsData();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Helper.displayErrorMessage(getActivity(), "Cancel");
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }


    private void openFilterDialog(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View subView = inflater.inflate(R.layout.filter_layout, null);

        Spinner rating = subView.findViewById(R.id.select_rating);
        final Spinner size = subView.findViewById(R.id.select_car_size);

        ArrayAdapter<String> ratingAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,ratingOptions);
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,carSizes);
        rating.setAdapter(ratingAdapter);
        size.setAdapter(sizeAdapter);

        rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRating = ratingOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSize = carSizes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter Options");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectedSize== null && selectedRating == null){
                    Helper.displayErrorMessage(getActivity(), "Nothing is selected");
                }else{
                    getCarsData();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Helper.displayErrorMessage(getActivity(), "Cancel");
            }
        });

        builder.show();
    }


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
            return (distance);

        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }


}
