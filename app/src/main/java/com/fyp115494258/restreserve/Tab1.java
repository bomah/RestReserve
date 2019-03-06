package com.fyp115494258.restreserve;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.Model.MyResponse;
import com.fyp115494258.restreserve.Model.Rating;
import com.fyp115494258.restreserve.Model.Reservation;
import com.fyp115494258.restreserve.Model.ReservationSlot;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.Model.Sender;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.fyp115494258.restreserve.ViewHolder.TimeViewHolder;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Tab1 extends Fragment implements OnMapReadyCallback {


    TextView  restaurant_address, restaurant_description,restaurant_phoneNumber;





    FirebaseDatabase database;
    DatabaseReference restaurant;
    Restaurant currentRestaurant;

    DatabaseReference reservation;

    //


    String RestaurantId = "";
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;

    DatabaseReference reservationSlot;

    RecyclerView recycler_time;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<ReservationSlot,TimeViewHolder> recyclerAdapter;


    //
    EditText edtChooseDate;

    Button btnChooseDate;
    TextView txtChooseDate;


    DatePickerDialog datePickerDialog;

    int year;
    int month;
    int dayOfMonth;

    Calendar calendar;

    String dateChoosen;

    String currentRestId;

    String restId;

    String dateRestaurantId;

    //People
    TextView txtNumOfPeople;
    CardView btnAddPeople;
    CardView btnMinusPeople;
    int peopleCount = 2;


    Button btnDetails;

    String dateSelected;
    String time;

    Reservation newReservation;


    //Reservation
    TextView txtRestaurantName;
    TextView txtDate;
    TextView txtTime;
    TextView txtNumberOfPeople;

    TextView txtPersonName;

    ReservationSlot peopleSelected;

    int updatedNumberOfPeople;

    String reservationSlotKey;
    TextView txtReservationSlotKey;

    ReservationSlot resSlot;


    //Rating
    FloatingActionButton btnRating;
   RatingBar ratingBar;

    DatabaseReference ratingTbl;



    //Map
    Button btnMap;


    private GoogleMap mMap;

    MapView mapView;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;


    Location mLastLocation;

    Marker mCurrentMarker;

    View v;

    View Tab1View;


    String choosenDate;

    String dateTime;


    TextView txtLocalEthos;


    TextView txtHours;










    public Tab1() {


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Tab1View=inflater.inflate(R.layout.fragment_tab1, container, false);


        //Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");


        reservationSlot = database.getReference("ReservationSlot");

        reservation=database.getReference("Reservation");

        ratingTbl=database.getReference("Rating");










        recycler_time=(RecyclerView)Tab1View.findViewById(R.id.recycler_time);
        //recycler_time.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recycler_time.setLayoutManager(layoutManager);









        //Rating



/*
        btnRating=(FloatingActionButton)Tab1View.findViewById(R.id.btnRating);


        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

*/










        restaurant_description = (TextView) Tab1View.findViewById(R.id.restaurant_description);

        restaurant_address = (TextView) Tab1View.findViewById(R.id.restaurant_address);
        restaurant_phoneNumber= (TextView) Tab1View.findViewById(R.id.restaurant_phoneNumber);

        txtLocalEthos=Tab1View.findViewById(R.id.txtLocalEthosDescription);


        txtHours=Tab1View.findViewById(R.id.txtHours);










        btnDetails = (Button) Tab1View.findViewById(R.id.btnDetails);

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterDetails();
            }
        });


        //https://stackoverflow.com/questions/25051246/how-to-use-supportmapfragment-inside-a-fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.gMap);
        mapFragment.getMapAsync(this);




        if(!Common.restId.isEmpty()){

            getDetailRestaurant(Common.restId);

            currentRestId=Common.restId;






        }


        return Tab1View;



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {





        mMap = googleMap;


        LatLng restaurantLocation = new LatLng(Common.currentRestaurant.getLat(),Common.currentRestaurant.getLng());



        mMap.addMarker(new MarkerOptions().position(restaurantLocation)
                .icon(BitmapDescriptorFactory.defaultMarker()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent restaurantLocation=new Intent(getActivity(),RestaurantLocation.class);
                startActivity(restaurantLocation);
            }
        });




    }



/*
    private void showRatingDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Not Great","Average","Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this restaurant")
                .setDescription("Please provide a rating")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please comment here...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(getActivity())
                .show();


    }
*/



    private void enterDetails() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);

        alertDialog.setTitle("Provide Reservation Details");

        alertDialog.setMessage("Please provide reservation details");

        LayoutInflater inflator = this.getLayoutInflater();

        View enter_details_layout = inflator.inflate(R.layout.enter_details_layout,null);



        btnChooseDate=enter_details_layout.findViewById(R.id.btnChooseDate);
        txtChooseDate=enter_details_layout.findViewById(R.id.txtChooseDate);





        //People
        txtNumOfPeople=enter_details_layout.findViewById(R.id.txtNumOfPeople);
        btnAddPeople=enter_details_layout.findViewById(R.id.btnAddPeople);
        btnMinusPeople=enter_details_layout.findViewById(R.id.btnMinusPeople);



        // txtNumOfPeople.setText(peopleCount);


        btnAddPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                peopleCount++;
                txtNumOfPeople.setText(String.valueOf(peopleCount).concat(" People"));

            }
        });


        btnMinusPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (peopleCount != 1) {
                    peopleCount--;
                    txtNumOfPeople.setText(String.valueOf(peopleCount).concat(" People"));


                }
                if (peopleCount == 1){
                    txtNumOfPeople.setText(String.valueOf(peopleCount).concat(" Person"));

                }

            }
        });




        //Referred to the following video:https://www.youtube.com/watch?v=CNGLsYPZd_o
        // to setup the date picker

        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getActivity(), R.style.datePickerDialog,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        txtChooseDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        dateSelected = txtChooseDate.getText().toString();


                        int mMonth=month+1;
                        String formattedMonth=""+mMonth;
                        String formattedDayOfMonth=""+dayOfMonth;

                        if(mMonth<10){

                             formattedMonth="0"+mMonth;
                        }

                        if(dayOfMonth<10){

                            formattedDayOfMonth="0"+dayOfMonth;
                        }

                        dateChoosen = String.valueOf(year+"-"+formattedMonth+"-"+formattedDayOfMonth) + currentRestId;

                        choosenDate=String.valueOf(year+"-"+formattedMonth+"-"+formattedDayOfMonth);


                        //getTimes();
                        //initRecycler();





                        //recyclerAdapter.notifyDataSetChanged();
                        //recycler_time.invalidate();



                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });












        alertDialog.setView(enter_details_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();

                //getTimes(dateChoosen);




                getTimes(dateChoosen,peopleCount);






            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();


            }
        });
        alertDialog.show();



    }


    public void getTimes(String dateChoosen,final int peopleCount) {


        //Query time = reservationSlot.orderByChild("restaurantId").equalTo(restaurantId);


        //reservationSlot.orderByChild("restaurantId").equalTo(currentRestId);

        // reservationSlot.orderByChild("dateRestaurantId").equalTo(dateChoosen);

        Query getReservationByDate = reservationSlot.orderByChild("dateRestaurantId")
                .equalTo(dateChoosen);




        FirebaseRecyclerOptions<ReservationSlot> options = new FirebaseRecyclerOptions.Builder<ReservationSlot>()
                .setQuery(getReservationByDate,ReservationSlot.class)
                .build();





        recyclerAdapter = new FirebaseRecyclerAdapter<ReservationSlot, TimeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TimeViewHolder viewHolder, int position, @NonNull ReservationSlot model) {


                //peopleSelected=model;
                //updatedNumberOfPeople=model.getNumberOfPeople()-peopleCount;


                //time=model.getTime();
                //ArrayList<String> times = new ArrayList<String>();
                //times.add(model.getTime());

                final TimeViewHolder mHolder=viewHolder;
                final ReservationSlot mModel=model;



                        viewHolder.btnTime.setText("");

                if(peopleCount <= model.getNumberOfPeople()) {






                    viewHolder.btnTime.setText(model.getTime());
                    //viewHolder.btnTime.setVisibility(Button.VISIBLE);


                }
                else if(peopleCount > model.getNumberOfPeople()&viewHolder.btnTime.equals("")){

                    //Toast.makeText(RestaurantDetail.this, "No times available" , Toast.LENGTH_SHORT).show();
                    //viewHolder.btnTime.setText("");
                    Toast.makeText(getActivity(), "No times available" , Toast.LENGTH_SHORT).show();
                    //viewHolder.btnTime.setVisibility(Button.GONE);
                    viewHolder.llTime.setLayoutParams(viewHolder.params);

                }
                else if (peopleCount > model.getNumberOfPeople()){

                    viewHolder.llTime.setLayoutParams(viewHolder.params);
                    // viewHolder.btnTime.setVisibility(View.GONE);


                }


                //viewHolder.btnTime.setLayoutParams(new RecyclerView.LayoutParams(0, 0));







                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });


                final ReservationSlot clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                                                    @Override
                                                    public void onClick(View view, int position, boolean isLongClick) {





                                                        //Toast.makeText(getActivity(), "" + clickItem.getTime(), Toast.LENGTH_SHORT).show();
                                                        updatedNumberOfPeople=clickItem.getNumberOfPeople()-peopleCount;
                                                        time=clickItem.getTime();
                                                        reservationSlotKey=recyclerAdapter.getRef(position).getKey();
                                                        //adapter.getItem(position);




                                                        makeReservation(recyclerAdapter.getItem(position));


                                                    }
                                                }

                );



            }

            @NonNull
            @Override
            public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.timeslot_item,parent,false);
                return new TimeViewHolder(itemView);

            }
        };

        //recyclerAdapter.startListening();
        //recyclerAdapter.notifyDataSetChanged();
        recycler_time.setAdapter(recyclerAdapter);
        //recyclerAdapter.notifyDataSetChanged();

        recyclerAdapter.startListening();






    }


    private void makeReservation(final ReservationSlot resSlot) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Reservation Details");

        alertDialog.setMessage("Your Reservation Details");

        LayoutInflater inflator = this.getLayoutInflater();

        View make_reservation_layout = inflator.inflate(R.layout.make_reservation_layout,null);


        txtRestaurantName = make_reservation_layout.findViewById(R.id.txtRestaurantName);

        txtDate = make_reservation_layout.findViewById(R.id.txtDate);
        txtTime= make_reservation_layout.findViewById(R.id.txtTime);
        txtNumberOfPeople= make_reservation_layout.findViewById(R.id.txtNumberOfPeople);


        txtRestaurantName.setText(currentRestaurant.getName());
        //txtPersonName.setText(Common.currentUser.getFirstName());

        txtDate.setText(dateSelected);
        txtTime.setText(time);
        txtNumberOfPeople.setText(String.valueOf(peopleCount).concat( " People"));
        //txtReservationSlotKey.setText(reservationSlotKey);


        dateTime=choosenDate+"-"+time;



        newReservation=new Reservation(currentRestId,txtRestaurantName.getText().toString(),Common.currentUser.getFirstName()+" "+Common.currentUser.getLastName(),Common.currentUser.getPhoneNumber(),Common.currentRestaurant.getAdminEmail(),dateSelected,txtTime.getText().toString(),dateTime,peopleCount);


        alertDialog.setView(make_reservation_layout);
        alertDialog.setIcon(R.drawable.ic_done_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();

                if(newReservation!=null)
                {
                    reservation.push().setValue(newReservation);
                    
                    
                    
                    
                    
                    
                    Toast.makeText(getActivity(), "" + txtRestaurantName.getText().toString() + " Successfully reserved", Toast.LENGTH_SHORT).show();


                    sendNotificationReservation(reservationSlotKey);
                    
                    

                    resSlot.setNumberOfPeople(updatedNumberOfPeople);
                    reservationSlot.child(reservationSlotKey).setValue(resSlot);



                }




            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();


            }
        });
        alertDialog.show();
    }

    private void sendNotificationReservation(String reservationSlotKey) {


    }


    private void getDetailRestaurant(String restaurantId) {

        // restaurant.child(restaurantId).addValueEventListener(new ValueEventListener(){

        restaurant.child(restaurantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentRestaurant = dataSnapshot.getValue(Restaurant.class);

                Common.currentRestaurant=currentRestaurant;



                restaurant_address.setText(currentRestaurant.getAddress());



                restaurant_description.setText(currentRestaurant.getDescription());

                restaurant_phoneNumber.setText(currentRestaurant.getPhoneNumber());

                txtLocalEthos.setText(currentRestaurant.getLocalEthos());

                txtHours.setText(currentRestaurant.getHours());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
