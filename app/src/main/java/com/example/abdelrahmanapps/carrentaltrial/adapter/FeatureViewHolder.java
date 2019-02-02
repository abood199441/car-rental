package com.example.abdelrahmanapps.carrentaltrial.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.R;

public class FeatureViewHolder extends RecyclerView.ViewHolder{

    public TextView title;
    public TextView detail;

    public FeatureViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.car_feature_title);
        detail = itemView.findViewById(R.id.car_price_title);

    }
}
