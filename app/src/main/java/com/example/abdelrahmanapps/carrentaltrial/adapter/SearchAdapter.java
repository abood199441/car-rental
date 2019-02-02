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
import com.example.abdelrahmanapps.carrentaltrial.models.SearchObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{

    private static final String TAG = SearchAdapter.class.getSimpleName();

    private Context context;

    private List<MyCar> searchList;





    public SearchAdapter(Context context, List<MyCar> searchList) {
        this.context = context;
        this.searchList = searchList;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_car_layout, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        final MyCar sObject = searchList.get(position);
        holder.carBrand.setText(sObject.getBrandData());
        holder.carName.setText(sObject.getModel());
        holder.carPrice.setText(sObject.getPrice() + "JD");
        holder.carDuration.setText("Day");
        holder.carFeature.setText(sObject.getDescription());
        holder.carLocation.setText(sObject.getAddress());
        Picasso.get().load(sObject.getImageURL()).into(holder.carImage);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent carIntent = new Intent(context, ProductActivity.class);
                carIntent.putExtra("CARBUNDLE",sObject.toBundle());
                context.startActivity(carIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}
