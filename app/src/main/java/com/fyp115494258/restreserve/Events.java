package com.fyp115494258.restreserve;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.Model.Event;
import com.fyp115494258.restreserve.Model.FoodMenuItem;
import com.fyp115494258.restreserve.Model.Reservation;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.EventViewHolder;
import com.fyp115494258.restreserve.ViewHolder.MainCourseViewHolder;
import com.fyp115494258.restreserve.ViewHolder.ReservationViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminReservationViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.fyp115494258.restreserve.ViewHolder.StartersViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Events extends AppCompatActivity {





    RecyclerView recyclerViewEvent;
    RecyclerView recyclerViewUpcomingEvent;



    RecyclerView.LayoutManager layoutManagerEvent;
    RecyclerView.LayoutManager layoutManagerUpcomingEvent;

    FirebaseDatabase database;
    DatabaseReference eventTbl;
    DatabaseReference upcomingEventTbl;



    FirebaseRecyclerAdapter<Event, EventViewHolder> eventAdapter;

    FirebaseRecyclerAdapter<Event, EventViewHolder> upcomingEventAdapter;


    //https://www.youtube.com/watch?v=fItHmbRQUW8&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=9
    //https://www.youtube.com/watch?v=5sz8sjg7hxA&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=42

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        database = FirebaseDatabase.getInstance();


        eventTbl = database.getReference("Event");


        upcomingEventTbl = database.getReference("UpcomingEvent");


        recyclerViewEvent=(RecyclerView) findViewById(R.id.recyclerEvents);
        recyclerViewUpcomingEvent=(RecyclerView)findViewById(R.id.recyclerUpcomingEvents);

        layoutManagerEvent = new LinearLayoutManager(Events.this,LinearLayoutManager.VERTICAL,false);
        layoutManagerUpcomingEvent = new LinearLayoutManager(Events.this,LinearLayoutManager.VERTICAL,false);


        recyclerViewEvent.setLayoutManager(layoutManagerEvent);
        recyclerViewUpcomingEvent.setLayoutManager(layoutManagerUpcomingEvent);










        loadEvent();

        loadUpcomingEvent();







    }


    private void loadEvent() {

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(eventTbl,Event.class)
                .build();

        eventAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder viewHolder, int position, @NonNull Event model) {



                viewHolder.txtEventName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imgEvent);

                viewHolder.txtEventDescription.setText(model.getDescription());






            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_layout,parent,false);
                return new EventViewHolder(itemView);
            }
        };
        eventAdapter.startListening();
        recyclerViewEvent.setAdapter(eventAdapter);

    }



    private void loadUpcomingEvent() {

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(upcomingEventTbl,Event.class)
                .build();

        upcomingEventAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder viewHolder, int position, @NonNull Event model) {



                viewHolder.txtEventName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imgEvent);

                viewHolder.txtEventDescription.setText(model.getDescription());






            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_layout,parent,false);
                return new EventViewHolder(itemView);
            }
        };
        upcomingEventAdapter.startListening();
        recyclerViewUpcomingEvent.setAdapter(upcomingEventAdapter);

    }



}
