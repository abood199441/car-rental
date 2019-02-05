package com.example.abdelrahmanapps.carrentaltrial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.abdelrahmanapps.carrentaltrial.fragment.MyCarsFragment;
import com.example.abdelrahmanapps.carrentaltrial.fragment.ProfileFragment;
import com.example.abdelrahmanapps.carrentaltrial.fragment.ReservationFragment;
import com.example.abdelrahmanapps.carrentaltrial.utils.Constants;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = AccountActivity.class.getSimpleName();
    private String fragmentTag;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment accountFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragments = null;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    fragments = new ProfileFragment();
                    fragmentTag ="profile_frag";
                    break;
                case R.id.navigation_reservation:
                    fragments = new ReservationFragment();
                    fragmentTag ="nav_res";
                    break;
                case R.id.navigation_mycars:
                    fragments = new MyCarsFragment();
                    fragmentTag ="nav_cars";
                    break;
            }

            fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragments, fragmentTag).commit();
            overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        accountFragment = new ProfileFragment();
        fragmentTag ="profile_frag";
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, accountFragment, fragmentTag).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent();
        back.setClass(this, HomeActivity.class);
        startActivity(back);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == Constants.REQUEST_IMAGE_LOCAL) {

            if(resultCode == RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            //        carPicture.setImageBitmap(selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte[] imgBytes = stream.toByteArray();
                    uploadImage(imgBytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Helper.displayErrorMessage(this, "Something went wrong");
                }

            }else {
                Helper.displayErrorMessage(this, "You have to pick an image");
            }

        }
    }
    private void uploadImage(byte[] imgBytes){

        UploadTask uploadTask = UserData.storageReference.child("profilePic.png").putBytes(imgBytes);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return UserData.storageReference.child("profilePic.png").getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    //update user profile picture URL and refresh accountFragment.
                    UserData.currentUser.updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            refreshFragment();
                        }
                    });
                } else {
                    Helper.displayErrorMessage(AccountActivity.this,"Error uploading image to the servers.");
                }
            }
        });


    }
    private void refreshFragment(){
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentByTag(fragmentTag);
        fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false);
        }
        fragmentTransaction.detach(currentFragment).attach(currentFragment).commit();
    }
}




