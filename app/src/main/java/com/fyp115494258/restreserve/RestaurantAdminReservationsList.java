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
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminReservationViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RestaurantAdminReservationsList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reservation,RestaurantAdminReservationViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference reservation;

    ////https://www.youtube.com/watch?v=k1RUOexThGs&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_admin_reservations_list);


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

        Query getReservationByEmail= reservation.orderByChild("adminEmail").equalTo(Common.currentUser.getEmail());


        FirebaseRecyclerOptions<Reservation> reservationOptions = new FirebaseRecyclerOptions.Builder<Reservation>()
                .setQuery(getReservationByEmail,Reservation.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Reservation, RestaurantAdminReservationViewHolder>(reservationOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantAdminReservationViewHolder viewHolder, int position, @NonNull Reservation model) {

                viewHolder.txtPersonName.setText(model.getPersonName());
                viewHolder.txtPersonPhoneNumber.setText(model.getPersonPhoneNumber());

                viewHolder.txtDate.setText(model.getDate());
                viewHolder.txtTime.setText(model.getTime());
                viewHolder.txtNumberOfPeople.setText(String.valueOf(model.getNumberOfPeople()).concat(" People"));


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

