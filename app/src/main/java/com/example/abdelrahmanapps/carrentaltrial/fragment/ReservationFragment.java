package com.example.abdelrahmanapps.carrentaltrial.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdelrahmanapps.carrentaltrial.R;
import com.example.abdelrahmanapps.carrentaltrial.adapter.CustomFragmentPageAdapter;


public class ReservationFragment extends Fragment {

    private static final String TAG = ReservationFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public ReservationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        getActivity().setTitle("Reservation");

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomFragmentPageAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
