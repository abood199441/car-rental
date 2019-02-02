package com.example.abdelrahmanapps.carrentaltrial;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.abdelrahmanapps.carrentaltrial.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback {

    boolean PermissionGranted = false;
    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLocation;
    GoogleMap mMap;
    Marker marker;
    FusedLocationProviderClient mFusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_destination);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_PERMISSION_LOCATION);
        } else {
            PermissionGranted = true;
        }

        if (PermissionGranted) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
             mFusedLocationProviderClient.getLastLocation()
             .addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        currentLocation  = (Location) task.getResult();
                        Log.i("SUPERSECRET",""+currentLocation);

                        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        marker  = mMap.addMarker(new MarkerOptions().position(currentLatLng)
                                .title("Your current location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,14f));

                        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(LatLng latLng) {
                                updateMarker(latLng);
                            }
                        });
                                        }
                }
            });
        }
    }

    private void updateMarker(LatLng latLng){
        marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Pick up location"));
        marker.getPosition();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }



}
