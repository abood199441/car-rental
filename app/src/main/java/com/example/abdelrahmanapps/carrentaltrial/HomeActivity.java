package com.example.abdelrahmanapps.carrentaltrial;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import com.example.abdelrahmanapps.carrentaltrial.fragment.BookingFragment;
import com.example.abdelrahmanapps.carrentaltrial.fragment.BusinessFragment;
import com.example.abdelrahmanapps.carrentaltrial.fragment.InformationFragment;
import com.example.abdelrahmanapps.carrentaltrial.fragment.SearchCarFragment;

import com.example.abdelrahmanapps.carrentaltrial.utils.Constants;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.google.firebase.auth.FirebaseAuth;

import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity {


    private static final String TAG = HomeActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private Fragment homeFragment;

    private ShareActionProvider mShareActionProvider;
    ImageView profileImage;
    NavigationView navigationView;
    TextView profileName;
    String[] permissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //side drawer for options.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        checkPemissions();

        //default fragment for the home activity.
        homeFragment = new BookingFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, homeFragment).commit();


        navigationView = findViewById(R.id.nav_view);
        profileImage = navigationView.getHeaderView(0).findViewById(R.id.nav_header_profPic);
        profileName = navigationView.getHeaderView(0).findViewById(R.id.profile_name);

        //drawer's profile picture listener.
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent();
                profileIntent.setClass(HomeActivity.this, AccountActivity.class);
                startActivity(profileIntent);
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
            }
        });

        //fragment/activity switching in case one of the options in the menu was selected.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.action_booking) {
                    homeFragment = new BookingFragment();
                } else if (id == R.id.action_search) {
                    homeFragment = new SearchCarFragment();

                } else if (id == R.id.action_share) {
                    //mShareActionProvider = (ShareActionProvider) item.getActionProvider();
                    //shareText();
                } else if (id == R.id.action_profile) {
                    Intent profileIntent = new Intent();
                    profileIntent.setClass(HomeActivity.this, AccountActivity.class);
                    startActivity(profileIntent);
                    overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                    finish();

                } else if (id == R.id.action_about) {
                    homeFragment = new BusinessFragment();
                } else if (id == R.id.action_info) {
                    homeFragment = new InformationFragment();
                } else if (id == R.id.action_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(HomeActivity.this, MainActivity.class);
                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(logoutIntent);
                    finish();
                }

                changeFragment(homeFragment);

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        checkRequests();

    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_content, fragment).commit();
        overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //setting UI for the current user (image and name).
        profileName.setText(UserData.currentUser.getDisplayName());
        if(UserData.currentUser.getPhotoUrl()!=null) {

            Picasso.get()
                    .load(UserData.currentUser.getPhotoUrl())
                    .resize(128,128)
                    .placeholder(R.drawable.profile)
                    .into(profileImage);
        }
    }

    private void checkPemissions(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            return;
        }else{
            ActivityCompat.requestPermissions(this,permissions,Constants.REQUEST_PERMISSIONS_STORAGE_LOCATION );
        }

    }



    /* public void shareText() {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String shareBodyText = "Download and share this app";
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Car Rental");
                    intent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                    startActivity(Intent.createChooser(intent, "Choose sharing method"));
                }
            */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You are about to exit the app, Are you sure?");
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HomeActivity.super.onBackPressed();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == Constants.REQUEST_PERMISSIONS_STORAGE_LOCATION) {
            //if permissions needed are granted continue.
            if (grantResults.length > 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Helper.displayErrorMessage(this,"thank you");

            } else {
                //if permissions needed are not granted either provide permissions again or exist the application.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Unable to proceed without the following permissions: \n" +
                        "1. Need Storage permission for access to car/personal profile pictures. \n" +
                        "2. Need Location permission for access to current user location.")
                        .setPositiveButton("Grant permission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPemissions();
                            }
                        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            return;
        }

    }


    private void checkRequests(){

    }
}






