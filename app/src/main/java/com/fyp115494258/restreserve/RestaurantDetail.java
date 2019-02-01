package com.fyp115494258.restreserve;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.Model.Reservation;
import com.fyp115494258.restreserve.Model.ReservationSlot;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.fyp115494258.restreserve.ViewHolder.TimeViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class RestaurantDetail extends AppCompatActivity {

    //Referred to the following video: https://www.youtube.com/watch?v=T19qTLVDFV0&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=4

    TextView restaurant_name, restaurant_location, restaurant_description,restaurant_phoneNumber;
    ImageView restaurant_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ElegantNumberButton numberButton;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);


        //Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");


        reservationSlot = database.getReference("ReservationSlot");

        reservation=database.getReference("Reservation");




        //Time Slot
        recycler_time=(RecyclerView)findViewById(R.id.recycler_time);
        //recycler_time.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycler_time.setLayoutManager(layoutManager);




        restaurant_description = (TextView) findViewById(R.id.restaurant_description);
        restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        restaurant_location = (TextView) findViewById(R.id.restaurant_location);
        restaurant_phoneNumber= (TextView) findViewById(R.id.restaurant_phoneNumber);
        restaurant_image = (ImageView) findViewById(R.id.img_restaurant);



        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        btnDetails = (Button) findViewById(R.id.btnDetails);

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterDetails();
            }
        });




        //Get food ID from Intent
        if(getIntent() != null)
            RestaurantId = getIntent().getStringExtra("RestaurantId");
        if(!RestaurantId.isEmpty()){
            //getTimes(RestaurantId);
            getDetailRestaurant(RestaurantId);
            //getTimes(RestaurantId);
            currentRestId = RestaurantId;


        }
    }

    private void enterDetails() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantDetail.this);

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

                datePickerDialog = new DatePickerDialog(RestaurantDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        txtChooseDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        dateSelected = txtChooseDate.getText().toString();



                        dateChoosen = txtChooseDate.getText().toString() + currentRestId;

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






        recyclerAdapter = new FirebaseRecyclerAdapter<ReservationSlot, TimeViewHolder>(ReservationSlot.class,
                R.layout.timeslot_item,
                TimeViewHolder.class,
                //reservationSlot.orderByChild("restaurantId").equalTo(restaurantId)

               // reservationSlot.orderByChild("dateRestaurantId").equalTo(dateChoosen)
                //reservationSlot.orderByChild("time")
                reservationSlot.orderByChild("dateRestaurantId").equalTo(dateChoosen)) {
            @Override
            protected void populateViewHolder(TimeViewHolder viewHolder, ReservationSlot model, int position) {



                //peopleSelected=model;
                //updatedNumberOfPeople=model.getNumberOfPeople()-peopleCount;


                //time=model.getTime();


                    viewHolder.btnTime.setText("");

                    if(peopleCount <= model.getNumberOfPeople()) {






                        viewHolder.btnTime.setText(model.getTime());
                        //viewHolder.btnTime.setVisibility(Button.VISIBLE);


                    }
                    else if(peopleCount > model.getNumberOfPeople()&viewHolder.btnTime.equals("")){

                        //Toast.makeText(RestaurantDetail.this, "No times available" , Toast.LENGTH_SHORT).show();
                        //viewHolder.btnTime.setText("");
                        Toast.makeText(RestaurantDetail.this, "No times available" , Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(RestaurantDetail.this, "" + clickItem.getTime(), Toast.LENGTH_SHORT).show();
                                                        updatedNumberOfPeople=clickItem.getNumberOfPeople()-peopleCount;
                                                        time=clickItem.getTime();
                                                        reservationSlotKey=recyclerAdapter.getRef(position).getKey();
                                                        //adapter.getItem(position);




                                                        makeReservation(recyclerAdapter.getItem(position));


                                                    }
                                                }

                );


            }
        };
        //recyclerAdapter.notifyDataSetChanged();
        recycler_time.setAdapter(recyclerAdapter);
        //recyclerAdapter.notifyDataSetChanged();







    }

    private void makeReservation(final ReservationSlot resSlot) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantDetail.this);

        alertDialog.setTitle("Reservation Details");

        alertDialog.setMessage("Please provide information");

        LayoutInflater inflator = this.getLayoutInflater();

        View make_reservation_layout = inflator.inflate(R.layout.make_reservation_layout,null);


        txtRestaurantName = make_reservation_layout.findViewById(R.id.txtRestaurantName);
        txtPersonName= make_reservation_layout.findViewById(R.id.txtPersonName);
        txtDate = make_reservation_layout.findViewById(R.id.txtDate);
        txtTime= make_reservation_layout.findViewById(R.id.txtTime);
        txtNumberOfPeople= make_reservation_layout.findViewById(R.id.txtNumberOfPeople);
        txtReservationSlotKey= make_reservation_layout.findViewById(R.id.txtReservationSlotKey);

        txtRestaurantName.setText(currentRestaurant.getName());
        txtPersonName.setText(Common.currentUser.getName());

        txtDate.setText(dateSelected);
        txtTime.setText(time);
        txtNumberOfPeople.setText(String.valueOf(peopleCount).concat( " People"));
        //txtReservationSlotKey.setText(reservationSlotKey);



        newReservation=new Reservation(currentRestId,txtRestaurantName.getText().toString(),txtPersonName.getText().toString(),Common.currentUser.getPhoneNumber(),Common.currentRestaurant.getAdminPhoneNumber(),txtDate.getText().toString(),txtTime.getText().toString(),peopleCount);


        alertDialog.setView(make_reservation_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();

                if(newReservation!=null)
                {
                    reservation.push().setValue(newReservation);
                    Toast.makeText(RestaurantDetail.this, "" + txtRestaurantName.getText().toString() + " Successfully reserved", Toast.LENGTH_SHORT).show();



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


    private void getDetailRestaurant(String restaurantId) {


        restaurant.child(restaurantId).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                currentRestaurant = dataSnapshot.getValue(Restaurant.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentRestaurant.getImage()).into(restaurant_image);

                collapsingToolbarLayout.setTitle(currentRestaurant.getName());

                restaurant_location.setText(currentRestaurant.getLocation());

                restaurant_name.setText(currentRestaurant.getName());

                restaurant_description.setText(currentRestaurant.getDescription());

                restaurant_phoneNumber.setText(currentRestaurant.getPhoneNumber());

                Common.currentRestaurant=currentRestaurant;



            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

    }


}
