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
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminReservationViewHolder;
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
public class RestaurantAdminUpcomingReservations extends Fragment {



    View RestaurantAdminUpcomingReservationsView;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reservation,RestaurantAdminReservationViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference reservation;



    public RestaurantAdminUpcomingReservations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RestaurantAdminUpcomingReservationsView=inflater.inflate(R.layout.fragment_restaurant_admin_upcoming_reservations, container, false);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        reservation = database.getReference("Reservation");

        //Init
        recyclerView = RestaurantAdminUpcomingReservationsView.findViewById(R.id.listUpcomingReservations);
       // recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);




        loadReservations();


        return RestaurantAdminUpcomingReservationsView;









    }


    private void loadReservations() {


        Date todayDate= Calendar.getInstance().getTime();





        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String todayString=formatter.format(todayDate);

        //Toast.makeText(getActivity(), todayString, Toast.LENGTH_SHORT).show();



        Query getReservationByDateTime = reservation.orderByChild("dateTime")
                //.startAt("2019-01-01-00-00").endAt(todayString);
                .startAt(todayString);




        FirebaseRecyclerOptions<Reservation> reservationOptions = new FirebaseRecyclerOptions.Builder<Reservation>()
                .setQuery(getReservationByDateTime,Reservation.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Reservation, RestaurantAdminReservationViewHolder>(reservationOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantAdminReservationViewHolder viewHolder, int position, @NonNull Reservation model) {




                final RestaurantAdminReservationViewHolder mHolder=viewHolder;
                final Reservation mModel=model;

                Query getReservationByEmail= reservation.orderByChild("adminEmail").equalTo(Common.currentUser.getEmail());

                getReservationByEmail.addValueEventListener(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                       mHolder.txtPersonName.setText(mModel.getPersonName());
                                                       mHolder.txtPersonPhoneNumber.setText(mModel.getPersonPhoneNumber());

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
            public RestaurantAdminReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_admin_reservation_layout,parent,false);
                return new RestaurantAdminReservationViewHolder(itemView);


            }
        };
        adapter.startListening();
        //adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }




}
