package com.fyp115494258.restreserve;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp115494258.restreserve.Model.ReservationSlot;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class RestaurantAdminRestaurantDetail extends AppCompatActivity  {

    TextView restaurant_name, restaurant_description, restaurant_phoneNumber;
    ImageView restaurant_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ElegantNumberButton numberButton;
    FirebaseDatabase database;
    DatabaseReference restaurant;
    Restaurant currentRestaurant;

    String RestaurantId = "";
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;


    Restaurant rest;

    Button btnTimePicker;
    TimePickerDialog timePickerDialog;

    TimePicker timePicker;
    EditText edtTime;

    ReservationSlot newReservationSlot;

    String currentRestId="";

    EditText edtRestaurantId;

    int mHour,mMin;

    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    EditText edtChooseTime;

    DatabaseReference reservationSlot;

    EditText edtChoosenTime;

    String choosenTime;

    int timeChoosen;

    TextView txtChoosenTime;

    //


    //
    EditText edtChooseDate;

    DatePickerDialog datePickerDialog;

    int year;
    int month;
    int dayOfMonth;

    String dateRestaurantId;


    //People
    TextView txtNumOfPeople;
    CardView btnAddPeople;
    CardView btnMinusPeople;
    int peopleCount=2;


    int timeInt;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_admin_restaurant_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");

        //
        reservationSlot = database.getReference("ReservationSlot");

        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);


        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTimes();
            }
        });
















        restaurant_description = (TextView) findViewById(R.id.restaurant_description);
        restaurant_name = (TextView) findViewById(R.id.restaurant_name);

        restaurant_phoneNumber= (TextView) findViewById(R.id.restaurant_phoneNumber);
        restaurant_image = (ImageView) findViewById(R.id.img_restaurant);

        //
        //restaurant_adminPhoneNumber= (TextView) findViewById(R.id.restaurant_adminPhoneNumber);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get food ID from Intent
        if(getIntent() != null)
            RestaurantId = getIntent().getStringExtra("RestaurantId");
        if(!RestaurantId.isEmpty()){
            getDetailRestaurant(RestaurantId);

            getRestaurantId(RestaurantId);

        }

    }

    public void getRestaurantId(String restaurantId) {

        currentRestId=restaurantId;
    }



    //3rd iteration
    /*Referred to the following video https://www.youtube.com/watch?v=4wyA7HDhIOA
    to enable me to setup the timepicker


    //4th iteration
    //Referred to the following video:https://www.youtube.com/watch?v=CNGLsYPZd_o
        // to setup the date picker

    */


    private void addTimes() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantAdminRestaurantDetail.this);

        alertDialog.setTitle("Provide Reservation Time");

        alertDialog.setMessage("Please provide reservation time");

        LayoutInflater inflator = this.getLayoutInflater();

        View add_time_layout = inflator.inflate(R.layout.add_new_time_layout,null);


        // edtName = add_restaurant_layout.findViewById(R.id.edtName);

        edtChooseTime=add_time_layout.findViewById(R.id.edtChooseTime);
        edtRestaurantId=add_time_layout.findViewById(R.id.restaurant_id);

        //
        edtChooseDate=add_time_layout.findViewById(R.id.edtChooseDate);


        //People
        txtNumOfPeople=add_time_layout.findViewById(R.id.txtNumOfPeople);
        btnAddPeople=add_time_layout.findViewById(R.id.btnAddPeople);
        btnMinusPeople=add_time_layout.findViewById(R.id.btnMinusPeople);



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




        //
        edtChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(RestaurantAdminRestaurantDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edtChooseDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);


                        newReservationSlot = new ReservationSlot(edtChooseDate.getText().toString(), edtChooseTime.getText().toString(),edtRestaurantId.getText().toString(),dateRestaurantId,peopleCount);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });



        edtChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(RestaurantAdminRestaurantDetail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        //edtChooseTime.setText(hourOfDay + ":" + minute);

                        edtChooseTime.setText(String.format("%02d:%02d", hourOfDay, minute));

                        dateRestaurantId= edtChooseDate.getText().toString()+ edtRestaurantId.getText().toString();

                        //timeInt=hourOfDay



                        // newReservationSlot = new ReservationSlot(edtChooseTime.getText().toString(),edtRestaurantId.getText().toString());


                        newReservationSlot = new ReservationSlot(edtChooseDate.getText().toString(), edtChooseTime.getText().toString(),edtRestaurantId.getText().toString(),dateRestaurantId,peopleCount);




                    }
                },currentHour,currentMinute, false);

                timePickerDialog.show();
            }
        });




        edtRestaurantId.setText(currentRestId);

        //




        //newReservationSlot = new ReservationSlot(edtChooseDate.getText().toString(), edtChooseTime.getText().toString(),edtRestaurantId.getText().toString(),dateRestaurantId,peopleCount);

        // newReservationSlot = new ReservationSlot(edtChooseDate.getText().toString(), edtChooseTime.getText().toString(),edtRestaurantId.getText().toString());







        alertDialog.setView(add_time_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();

                if(newReservationSlot!=null)
                {
                    reservationSlot.push().setValue(new ReservationSlot(edtChooseDate.getText().toString(), edtChooseTime.getText().toString(),edtRestaurantId.getText().toString(),dateRestaurantId,peopleCount));


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
                Picasso.get().load(currentRestaurant.getImage()).into(restaurant_image);

                collapsingToolbarLayout.setTitle(currentRestaurant.getName());



                restaurant_name.setText(currentRestaurant.getName());

                restaurant_description.setText(currentRestaurant.getDescription());

                restaurant_phoneNumber.setText(currentRestaurant.getPhoneNumber());





            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

    }


}
