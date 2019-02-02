package com.example.abdelrahmanapps.carrentaltrial;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.abdelrahmanapps.carrentaltrial.customviews.DateBlock;
import com.example.abdelrahmanapps.carrentaltrial.models.BookingRequest;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {

    private static final String TAG = BookingActivity.class.getSimpleName();

    private ImageView carImage;

    private TextView pickUpLocation;

    private DateBlock startDateBlock, endDateBlock;

    private TextView dailyPrice, extraHour, tax, totalAmount;

    private TextView name, address, phone;


    private Bundle carData;

    private Calendar startCalendar;
    private Calendar endCalendar;
    private Calendar currentDate;
    private DateFormatSymbols symbols;
    private String[] AM_PM ;

    private String[] monthNames;
    private String[] days;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        carData = extras.getBundle("CARBUNDLE");
        carImage = findViewById(R.id.car_image);
        Picasso.get().load(carData.getString("IMAGE")).into(carImage);
        pickUpLocation =findViewById(R.id.pick_up_address);
        pickUpLocation.setText(carData.getString("ADDRESS"));
        startDateBlock = findViewById(R.id.pick_up_date);

        currentDate = Calendar.getInstance();
        symbols = new DateFormatSymbols(Locale.getDefault());
        AM_PM= symbols.getAmPmStrings();
        monthNames = symbols.getMonths();
        days = symbols.getWeekdays();

        startDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCalendar = Calendar.getInstance();
                DatePickerDialog(startDateBlock,startCalendar,currentDate);
            }
        });

        endDateBlock = findViewById(R.id.destination_date);
        endDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCalendar = Calendar.getInstance();
                DatePickerDialog(endDateBlock,endCalendar,startCalendar);
            }
        });
        dailyPrice = findViewById(R.id.book_daily_price);
        dailyPrice.setText(String.valueOf((Integer.parseInt(carData.getString("PRICE"))*24)));
        extraHour = findViewById(R.id.extra_hour);
        extraHour.setText(carData.getString("PRICE"));
        totalAmount = findViewById(R.id.total_amount);

        name = findViewById(R.id.customer_name);
        name.setText(carData.getString("NAME"));
        address = findViewById(R.id.customer_address);
        address.setText(carData.getString("ADDRESS"));
        phone = findViewById(R.id.customer_phone);
        phone.setText(carData.getString("PHONE"));


        Button bookNowButton =findViewById(R.id.book_now);
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!carData.getString("USERID").equals(UserData.currentUser.getUid())){
                    if(!(startCalendar==null || endCalendar==null)){
                        sendBookingRequest();
                    }
                    Helper.showMessageDialog(BookingActivity.this,"Have to pick the start and the end of the required renting period before being able to book the car");
                }else {
                    Helper.showMessageDialog(BookingActivity.this,"This car is yours, you cant book it.");
                }

            }
        });


    }

    private void DatePickerDialog(final DateBlock dateBlock,final Calendar calendar,final Calendar referencedCalendar){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                timePickerDialog(dateBlock,calendar,referencedCalendar);

            }
        };
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

         new DatePickerDialog(this, listener, year, month, day).show();

    }



    private void timePickerDialog(final DateBlock dateBlock,final Calendar calendar,final Calendar referencedCalendar){

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                //check if the time is valid and not in the past or before start time of the booking request.
                if(timeBetween(calendar,referencedCalendar)>=0){
                //valid time proceed to update UI
                    updateDateBlock(dateBlock,calendar);
                    updateCost();
                }else {
                    //invalid time entry
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatePickerDialog(dateBlock, calendar, referencedCalendar);
                        }
                    });
                    builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.setMessage("You have selected an invalid time please try again");
                    builder.create().show();
                }
            }
        };
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(this,listener,hour,
                        minute, DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }
    private void updateCost(){
        //update the total amount of the booking request.
        long hours = timeBetween(endCalendar,startCalendar);
        int mins = endCalendar.get(Calendar.MINUTE) - startCalendar.get(Calendar.MINUTE);
        if(mins>=30){
            hours++;
        }
        double price = hours*Integer.parseInt(extraHour.getText().toString());
        if(price<=0)return;
        totalAmount.setText(String.valueOf(price));

    }


    //calculate time between 2 dates in hours
    private long timeBetween(Calendar end, Calendar start){

        int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        int years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        long hours = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);

        days = days + years*365;
        hours = hours + days*24;
        return hours;
    }

    private void updateDateBlock(DateBlock dateBlock,Calendar calendar){
        dateBlock.setDayText(days[calendar.get(Calendar.DAY_OF_WEEK)]);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String monthName = monthNames[month];
        dateBlock.setMonthText(String.valueOf(dayOfMonth) + "/" + monthName + "/" + year );

        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        if(hour == 0) hour=12;

        String time = "At " + String.format("%02d",hour) + ":" + String.format("%02d",minute) + AM_PM[calendar.get(Calendar.AM_PM)] ;
        dateBlock.setTimeText(time);

    }


    private void sendBookingRequest(){

        UUID uuid = UUID.randomUUID();
        DatabaseReference database = FirebaseDatabase
                .getInstance()
                .getReference("Bookings")
                .child(carData.getString("USERID"))
                .child(carData.getString("CARID"))
                .child(UserData.currentUser.getUid())
                .child(uuid.toString());
        FirebaseUser user = UserData.currentUser;
        BookingRequest bookingRequest =
                new BookingRequest(startCalendar,endCalendar,user.getDisplayName(),user.getUid(),user.getPhoneNumber());

        database.setValue(bookingRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Helper.displayErrorMessage(BookingActivity.this,"Request sent successfully.");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);

                builder.setMessage("Request sent successfully");
                builder.setPositiveButton("Call car owner", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse( "tel:" + carData.getString("PHONE")));
                        startActivity(callIntent);
                    }
                });
                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent home = new Intent(BookingActivity.this,HomeActivity.class);
                        startActivity(home);
                        BookingActivity.this.finish();
                    }
                });
                builder.create().show();
            }
        });

    }

}
