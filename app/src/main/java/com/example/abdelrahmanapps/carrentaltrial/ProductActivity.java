package com.example.abdelrahmanapps.carrentaltrial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.models.MyCar;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    private static final String TAG = ProductActivity.class.getSimpleName();

    private ImageView carImage;

    private TextView mileage, fuelType, transmission, numOfSeats, hourlyPrice, dailyPrice;

    private ImageView favoriteImage;

    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        final Bundle carData = extras.getBundle("CARBUNDLE");



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(carData.getString("BRAND") + " - " + carData.get("MODEL"));

        carImage = findViewById(R.id.header_image);
        Picasso.get().load(carData.getString("IMAGE")).into(carImage);
        mileage = findViewById(R.id.mileage);
        mileage.setText(carData.getString("COUNTER"));
        fuelType = findViewById(R.id.fuel_type);
        fuelType.setText(carData.getString("GAS"));

        transmission = findViewById(R.id.transmission);
        transmission.setText(carData.getString("TRANSMISSION"));

        numOfSeats = findViewById(R.id.num_of_seats);
        numOfSeats.setText(carData.getString("SEATS"));

        hourlyPrice = findViewById(R.id.hourly_price);
        hourlyPrice.setText(carData.getString("PRICE"));
        dailyPrice = findViewById(R.id.daily_price);
        dailyPrice.setText(String.valueOf(Integer.parseInt(carData.getString("PRICE"))*24));

        favoriteImage = findViewById(R.id.favorite);

        ratingBar = findViewById(R.id.rating_bar);

        final Button bookingButton = findViewById(R.id.continue_booking);
        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bookingIntent = new Intent(ProductActivity.this, BookingActivity.class);
                bookingIntent.putExtra("CARBUNDLE",carData);
                startActivity(bookingIntent);
            }
        });





    }
}
