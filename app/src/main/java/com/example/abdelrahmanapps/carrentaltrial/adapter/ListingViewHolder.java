package com.example.abdelrahmanapps.carrentaltrial.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.R;

public class ListingViewHolder extends RecyclerView.ViewHolder {

    public ImageView carImage;
    public TextView carName;
    public View view;


    public ListingViewHolder(View itemView) {
        super(itemView);

        carImage = itemView.findViewById(R.id.car_category_image);
        carName = itemView.findViewById(R.id.car_category_name);
        view = itemView;
    }
}