package com.example.abdelrahmanapps.carrentaltrial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.example.abdelrahmanapps.carrentaltrial.models.MyCar;
import com.example.abdelrahmanapps.carrentaltrial.utils.Constants;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class AddCarActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private static final int REQUEST_PICTURE_CODE =1444;

    FirebaseUser user ;
    DatabaseReference ref;
    ImageView carPicture;

    Spinner brandSpinner,gasSpinner,seatNumberSpinner , statusSpinner,carSizeSpinner, transmissionSpinner;
    EditText description,model,priceHourEditText,milage;
    String[] brand
                 ,gas
                 ,seatNumber
                 ,status
                 ,carSize
                 ,transmission;

    byte[] imgBytes;
    ProgressBar imgUploadProgress;

    String brandData, gasData,seatNumberData,statusData,
            imageDownloadLink,descriptionData,modelData,sizeData,
            priceHourData,milageData, transmissionData,uuid,
            countryName,cityName,address;
    LatLng pickUpLocation;


    EditText location;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_layout);


        //initialize firebase things.

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Cars").child(user.getUid());


        //initialize UI elements.
        carPicture = findViewById(R.id.car_picture);
        gasSpinner = findViewById(R.id.fuelTypeSpinner);
        seatNumberSpinner = findViewById(R.id.seatNumberSpinner);
        brandSpinner = findViewById(R.id.brandSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        imgUploadProgress = findViewById(R.id.carImageBar);
        imgUploadProgress.setVisibility(View.INVISIBLE);
        carSizeSpinner = findViewById(R.id.carSizeSpinner);
        description = findViewById(R.id.car_description);
        milage = findViewById(R.id.carMilage);
        model = findViewById(R.id.car_model);
        transmissionSpinner = findViewById(R.id.transmissionSpinner);
        Button proceed = findViewById(R.id.makeCar);
        priceHourEditText = findViewById(R.id.carPrice);
        carPicture.setImageResource(R.drawable.no_photo);
        location = findViewById(R.id.add_car_location);
        imageDownloadLink="";

        carPicture.setOnClickListener(this);

        //initialize String arrays.
        brand = getResources().getStringArray(R.array.car_brand);
        gas = getResources().getStringArray(R.array.car_gas);
        seatNumber = getResources().getStringArray(R.array.car_option);
        status = getResources().getStringArray(R.array.car_status);
        carSize = getResources().getStringArray(R.array.car_size);
        transmission =getResources().getStringArray(R.array.car_transmission_array);

        //Initialize Spinners.
        initializeSpinner(seatNumberSpinner,seatNumber);
        initializeSpinner(brandSpinner,brand);
        initializeSpinner(statusSpinner,status);
        initializeSpinner(carSizeSpinner,carSize);
        initializeSpinner(gasSpinner,gas);
        initializeSpinner(transmissionSpinner, transmission);

        transmissionSpinner.setOnItemSelectedListener(this);
        carSizeSpinner.setOnItemSelectedListener(this);
        brandSpinner.setOnItemSelectedListener(this);
        statusSpinner.setOnItemSelectedListener(this);
        seatNumberSpinner.setOnItemSelectedListener(this);
        gasSpinner.setOnItemSelectedListener(this);
        proceed.setOnClickListener(this);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(AddCarActivity.this), Constants.PLACE_PICKER_REQUEST);
                    }catch (Exception e ){
                        Helper.displayErrorMessage(AddCarActivity.this,"Something went wrong pls try again later");
                        e.printStackTrace();
                    }
                    location.clearFocus();
                }

        });
    }


    private void initializeSpinner(Spinner spinner,String[] items){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.brandSpinner:
                brandData = brand[position];
                break;
            case R.id.carSizeSpinner:
                sizeData = carSize[position];
                break;
            case R.id.statusSpinner:
                statusData = status[position];
                break;
            case R.id.seatNumberSpinner:
                seatNumberData = seatNumber[position];
                break;
            case R.id.fuelTypeSpinner:
                gasData = gas[position];
                break;
            case R.id.transmissionSpinner:
                transmissionData = transmission[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.car_picture:
                    Intent getPicture = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    getPicture.setType("image/*");
                    getPicture.putExtra("outputX", 256);
                    getPicture.putExtra("outputY", 256);
                    getPicture.putExtra("crop", "true");
                    startActivityForResult(getPicture, REQUEST_PICTURE_CODE);
                break;
            case R.id.makeCar:

                    if (!description.getText().toString().isEmpty() &&
                            !model.getText().toString().isEmpty() &&
                            !priceHourEditText.getText().toString().isEmpty()&&
                            !milage.getText().toString().isEmpty() &&
                            !(pickUpLocation==null)) {
                        milageData = milage.getText().toString();
                        descriptionData = description.getText().toString();
                        modelData = model.getText().toString();
                        priceHourData = priceHourEditText.getText().toString();
                        if (imgBytes != null ) {
                            uploadCarData(imgBytes);
                        }else{
                            Helper.displayErrorMessage(this,"Please provide a car picture");
                        }

                    } else {
                        Helper.displayErrorMessage(this, "Please fill all the fields");
                    }
                    break;

        }


        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_PICTURE_CODE){
            if(resultCode == RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    carPicture.setImageBitmap(selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    imgBytes = stream.toByteArray();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                   Helper.displayErrorMessage(this, "Something went wrong");
                }

            }else {
                Helper.displayErrorMessage(this, "You have to pick an image");
            }
        }


        if (requestCode == Constants.PLACE_PICKER_REQUEST) {


            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                pickUpLocation = place.getLatLng();
                Geocoder geocoder = new Geocoder(this,Locale.getDefault());
                try {
                    List<Address> myList = geocoder.getFromLocation(pickUpLocation.latitude, pickUpLocation.longitude, 1);
                    countryName = myList.get(0).getCountryName();
                    cityName = myList.get(0).getLocality();
                    address = (String) place.getAddress();
                    location.setText(address);

                }catch (IOException e){

                }

            }


        }


    }

    public void uploadCarData(byte[] byteArray){
        uuid = UUID.nameUUIDFromBytes(byteArray).toString();
        final StorageReference currentStorageRef = UserData.carStorageReference.child(uuid);
        imgUploadProgress.setVisibility(View.VISIBLE);
        UploadTask uploadTask = currentStorageRef.putBytes(byteArray);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                imgUploadProgress.setProgress((int) (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()));
            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return currentStorageRef.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    imageDownloadLink = downloadUri.toString();
                    MyCar myCar = new MyCar(brandData, gasData, seatNumberData, statusData,
                            imageDownloadLink, modelData, descriptionData,priceHourData,
                            sizeData,transmissionData, milageData,uuid, pickUpLocation.latitude,
                            pickUpLocation.longitude,address);
                    //2 pictures would have the same uuid....
                    ref.child(uuid).setValue(myCar).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                    imgUploadProgress.setVisibility(View.INVISIBLE);
                } else {

                    Helper.displayErrorMessage(AddCarActivity.this ,"Error uploading the picture please try again");
                    // Handle failures
                    // ...
                }
            }
        });
    }



}
