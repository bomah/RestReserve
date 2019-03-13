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

public class UpcomingReservations extends Fragment {



    View UpcomingReservationsView;



    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reservation,ReservationViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference reservation;



    public UpcomingReservations() {
        // Required empty public constructor
    }

    //https://www.youtube.com/watch?v=W_H4kLoyFYc&list=PLxefhmF0pcPmtdoud8f64EpgapkclCllj&index=45
    //https://www.youtube.com/watch?v=k1RUOexThGs&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=3
    //https://www.youtube.com/watch?v=3Am-iad_Gkg

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UpcomingReservationsView=inflater.inflate(R.layout.fragment_upcoming_reservations, container, false);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        reservation = database.getReference("Reservation");

        //Init
        recyclerView = UpcomingReservationsView.findViewById(R.id.listUpcomingReservations);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);







        loadReservations();






        return UpcomingReservationsView;











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

        //https://stackoverflow.com/questions/1459656/how-to-get-the-current-time-in-yyyy-mm-dd-hhmisec-millisecond-format-in-java

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String todayString=formatter.format(todayDate);

        //Toast.makeText(getActivity(), todayString, Toast.LENGTH_SHORT).show();



        Query getReservationByPhone = reservation.orderByChild("dateTime")
                //.startAt("2019-01-01-00-00").endAt(todayString);
                .startAt(todayString);


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
