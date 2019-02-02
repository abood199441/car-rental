package com.example.abdelrahmanapps.carrentaltrial.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.models.MyCar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCarsListAdapter extends BaseAdapter {

    ArrayList<MyCar> myCars;
    LayoutInflater layoutInflater;
    public MyCarsListAdapter (ArrayList<MyCar> myCars, LayoutInflater layoutInflater){
        this.myCars = myCars;
        this.layoutInflater = layoutInflater;

    }

    @Override
    public int getCount() {

        return myCars.size();
    }

    @Override
    public Object getItem(int position) {
        return myCars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.single_car_layout,null);

        //initialize view elements
        TextView brand = convertView.findViewById(R.id.car_brand);
        TextView model = convertView.findViewById(R.id.car_model);
        TextView description = convertView.findViewById(R.id.category_description);
        ImageView carPic = convertView.findViewById(R.id.car_category_image);
        TextView carPrice = convertView.findViewById(R.id.price_per_day);
        TextView carLocation = convertView.findViewById(R.id.car_view_location);
        //fill in car info
        MyCar myCar =  myCars.get(position);
        carLocation.setText(myCar.getAddress());
        Picasso.get().load(Uri.parse(myCar.getImageURL())).into(carPic);
        brand.setText(myCar.getBrandData());
        model.setText(myCar.getModel());
        carPrice.setText(myCar.getPrice()+ "JD");
        description.setText(myCar.getDescription());
        return convertView;
    }
}
