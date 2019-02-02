package com.example.abdelrahmanapps.carrentaltrial.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdelrahmanapps.carrentaltrial.ProductActivity;
import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.models.MyCar;
import com.example.abdelrahmanapps.carrentaltrial.utils.Helper;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    private static final String TAG = CategoryAdapter.class.getSimpleName();

    private Context context;
    private List<MyCar> carList;

    public CategoryAdapter(Context context, List<MyCar> carList) {
        this.context = context;
        this.carList = carList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_car_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final MyCar carListObject = carList.get(position);
        holder.carType.setText(carListObject.getBrandData());
        holder.carName.setText(carListObject.getModel());
        holder.price.setText("JD" + carListObject.getPrice());
        holder.carLocation.setText(carListObject.getAddress());
        Picasso.get().load(carListObject.getImageURL()).into(holder.carImage);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent carIntent = new Intent(context, ProductActivity.class);
                carIntent.putExtra("CARBUNDLE",carListObject.toBundle());
                context.startActivity(carIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }
}
