package com.fyp115494258.restreserve;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.Reservation;
import com.fyp115494258.restreserve.ViewHolder.ReservationViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastReservations extends Fragment {



    View PastReservationsView;



    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reservation,ReservationViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference reservation;



    public PastReservations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PastReservationsView=inflater.inflate(R.layout.fragment_past_reservations, container, false);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        reservation = database.getReference("Reservation");

        //Init
        recyclerView = PastReservationsView.findViewById(R.id.listPastReservations);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);




        loadReservations();






        return PastReservationsView;











    }


    private void loadReservations() {

        Date todayDate= Calendar.getInstance().getTime();

        /*
        Date dt=new Date();
        Calendar c =Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE,1);
        dt=c.getTime();
        String todayDt = String.valueOf(dt);

        */

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String todayString=formatter.format(todayDate);

        Toast.makeText(getActivity(), todayString, Toast.LENGTH_SHORT).show();



        Query getReservationByPhone = reservation.orderByChild("dateTime")
                .startAt("2019-01-01-00-00").endAt(todayString);
                //.startAt(todayString);


        //Query getReservationByPhone = reservation.orderByChild("date")
        //    .startAt(todayDt);



        FirebaseRecyclerOptions<Reservation> reservationOptions = new FirebaseRecyclerOptions.Builder<Reservation>()
                .setQuery(getReservationByPhone,Reservation.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Reservation, ReservationViewHolder>(reservationOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ReservationViewHolder viewHolder, int position, @NonNull Reservation model) {



                final ReservationViewHolder mHolder=viewHolder;
                final Reservation mModel=model;

                Query userView= reservation.orderByChild("personPhoneNumber").equalTo(Common.currentUser.getPhoneNumber());

                userView.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mHolder.txtRestaurantName.setText(mModel.getRestaurantName());

                        mHolder.txtDate.setText(mModel.getDate());
                        mHolder.txtTime.setText(mModel.getTime());
                        mHolder.txtNumberOfPeople.setText(String.valueOf(mModel.getNumberOfPeople()).concat(" People"));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @NonNull
            @Override
            public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.reservation_layout,parent,false);
                return new ReservationViewHolder(itemView);


            }
        };
        adapter.startListening();
        //adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }






}
