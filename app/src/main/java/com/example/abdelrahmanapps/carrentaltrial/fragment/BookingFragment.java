package com.example.abdelrahmanapps.carrentaltrial.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.adapter.ListingAdapter;
import com.example.abdelrahmanapps.carrentaltrial.models.CarCategoryObject;

import java.util.ArrayList;
import java.util.List;


public class BookingFragment extends Fragment {

    private static final String TAG = BookingFragment.class.getSimpleName();

    private RecyclerView bookingRecyclerView;


    public BookingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        bookingRecyclerView = view.findViewById(R.id.car_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        bookingRecyclerView.setLayoutManager(gridLayoutManager);
        bookingRecyclerView.setHasFixedSize(true);

        ListingAdapter mAdapter = new ListingAdapter(getActivity(), getTextData());
        bookingRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private List<CarCategoryObject> getTextData() {
        String[] brands = getResources().getStringArray(R.array.car_brand);
        List<CarCategoryObject> carData = new ArrayList<>();

        for (String s : brands){
            carData.add(new CarCategoryObject("",s));
        }


        return carData;
    }

}
