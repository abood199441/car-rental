package com.example.abdelrahmanapps.carrentaltrial.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdelrahmanapps.carrentaltrial.CarCategoryActivity;
import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.models.CarCategoryObject;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingViewHolder>{

    private static final String TAG = ListingAdapter.class.getSimpleName();

    private Context context;

    private List<CarCategoryObject> brandList;


    public ListingAdapter(Context context, List<CarCategoryObject> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @Override
    public ListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_category_layout, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListingViewHolder holder, int position) {
        final CarCategoryObject carCategoryObject = brandList.get(position);

        holder.carName.setText(carCategoryObject.getImageName());
        //holder.carImage
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent carCategoryIntent = new Intent(context, CarCategoryActivity.class);
                carCategoryIntent.putExtra("BRAND",carCategoryObject.getImageName());
                context.startActivity(carCategoryIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }
}
