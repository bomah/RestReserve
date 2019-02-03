package com.fyp115494258.restreserve;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.Reservation;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.ReservationViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ReservationList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reservation,ReservationViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        reservation = database.getReference("Reservation");

        //Init
        recyclerView = findViewById(R.id.listReservations);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        loadReservations();




    }

    private void loadReservations() {

        Query getReservationByPhone = reservation.orderByChild("personPhoneNumber")
                .equalTo(Common.currentUser.getPhoneNumber());


        FirebaseRecyclerOptions<Reservation> reservationOptions = new FirebaseRecyclerOptions.Builder<Reservation>()
                .setQuery(getReservationByPhone,Reservation.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Reservation, ReservationViewHolder>(reservationOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ReservationViewHolder viewHolder, int position, @NonNull Reservation model) {

                viewHolder.txtPersonName.setText(model.getPersonName());

                viewHolder.txtDate.setText(model.getDate());
                viewHolder.txtTime.setText(model.getTime());
                viewHolder.txtNumberOfPeople.setText(String.valueOf(model.getNumberOfPeople()).concat(" People"));


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


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
