package com.example.abdelrahmanapps.carrentaltrial.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.R;


public class ReservationViewHolder extends RecyclerView.ViewHolder {

    public TextView carName;
    public TextView location;
    public TextView date;
    public TextView time;
    public TextView price;
    public ImageView pathImage;
    public View view;


    public ReservationViewHolder(View itemView) {
        super(itemView);

        carName = itemView.findViewById(R.id.car_name);
        location = itemView.findViewById(R.id.car_location);
        date = itemView.findViewById(R.id.pick_up_date);
        time = itemView.findViewById(R.id.pick_up_time);
        price = itemView.findViewById(R.id.rental_price);
        pathImage = itemView.findViewById(R.id.image_path);
        view = itemView;

    }
}
