package com.example.abdelrahmanapps.carrentaltrial.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.R;

public class SearchViewHolder extends RecyclerView.ViewHolder{

    public ImageView carImage;
    public TextView carName;
    public TextView carPrice;
    public TextView carFeature;
    public TextView carDuration;
    public TextView carBrand;
    public TextView carLocation;

    public View view;


    public SearchViewHolder(View itemView) {
        super(itemView);

        carImage = itemView.findViewById(R.id.car_category_image);
        carBrand = itemView.findViewById(R.id.car_brand);
        carName = itemView.findViewById(R.id.car_model);
        carPrice = itemView.findViewById(R.id.price_per_day);
        carFeature = itemView.findViewById(R.id.category_description);
        carDuration = itemView.findViewById(R.id.per_day);
        carLocation = itemView.findViewById(R.id.car_view_location);
        view = itemView;
    }
}
