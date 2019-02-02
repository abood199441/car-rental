package com.example.abdelrahmanapps.carrentaltrial.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public TextView carType;
    public TextView carName;
    public RatingBar ratingBar;
    public TextView price;
    public ImageView carImage;
    public TextView carDescription;
    public TextView carLocation;

    public View view;


    public CategoryViewHolder(View itemView) {
        super(itemView);
        carLocation = itemView.findViewById(R.id.car_view_location);
        carType = itemView.findViewById(R.id.car_brand);
        carName = itemView.findViewById(R.id.car_model);
        ratingBar = itemView.findViewById(R.id.rating_bar);
        price = itemView.findViewById(R.id.price_per_day);
        carImage = itemView.findViewById(R.id.car_category_image);
        carDescription = itemView.findViewById(R.id.category_description);
        view = itemView;
    }
}
