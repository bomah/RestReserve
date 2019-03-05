package com.fyp115494258.restreserve;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.Reservation;
import com.fyp115494258.restreserve.ViewHolder.ReservationViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminReservationViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RestaurantAdminReservationList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reservation,ReservationViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference reservation;


    ViewPager viewPager;
    RestaurantAdminTabsAdapterReservations tabAdapter;

    TabLayout tabLayout;

    Toolbar tBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_admin_reservation_list);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        reservation = database.getReference("Reservation");


        tBar=(Toolbar)findViewById(R.id.tBar);




        viewPager = (ViewPager) findViewById(R.id.pagerReservations);
        tabAdapter = new RestaurantAdminTabsAdapterReservations(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);






        tabLayout = (TabLayout) findViewById(R.id.tabLayoutReservations);



        tabLayout.setupWithViewPager(viewPager);



    }



}
