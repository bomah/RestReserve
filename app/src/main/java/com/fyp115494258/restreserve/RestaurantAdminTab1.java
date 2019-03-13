package com.fyp115494258.restreserve;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.Reservation;
import com.fyp115494258.restreserve.Model.ReservationSlot;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.fyp115494258.restreserve.ViewHolder.TimeViewHolder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantAdminTab1 extends Fragment implements OnMapReadyCallback {


    TextView restaurant_name, restaurant_description, restaurant_phoneNumber,restaurant_address;
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

    String dateChoosen="";

    String dateTime="";


    //People
    TextView txtNumOfPeople;
    CardView btnAddPeople;
    CardView btnMinusPeople;
    int peopleCount=2;


    private GoogleMap mMap;

    int timeInt;

    View RestaurantAdminTab1View;


    TextView txtLocalEthos;

    TextView txtHours;


    public RestaurantAdminTab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RestaurantAdminTab1View=inflater.inflate(R.layout.fragment_restaurant_admin_tab1, container, false);

        //Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");

        //
        reservationSlot = database.getReference("ReservationSlot");

        btnTimePicker = (Button)RestaurantAdminTab1View.findViewById(R.id.btnTimePicker);


        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTimes();
            }
        });



        //https://stackoverflow.com/questions/25051246/how-to-use-supportmapfragment-inside-a-fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.gMap);
        mapFragment.getMapAsync(this);





        txtLocalEthos=(TextView) RestaurantAdminTab1View.findViewById(R.id.txtLocalEthosDescription);

        txtHours=(TextView) RestaurantAdminTab1View.findViewById(R.id.txtHours);


        restaurant_description = (TextView) RestaurantAdminTab1View.findViewById(R.id.restaurant_description);

        restaurant_address=(TextView) RestaurantAdminTab1View.findViewById(R.id.restaurant_address);


        restaurant_phoneNumber= (TextView) RestaurantAdminTab1View.findViewById(R.id.restaurant_phoneNumber);
        restaurant_image = (ImageView) RestaurantAdminTab1View.findViewById(R.id.img_restaurant);




        if(!Common.restId.isEmpty()){

            getDetailRestaurant(Common.restId);

            currentRestId=Common.restId;


        }





        return RestaurantAdminTab1View;
    }

    //https://www.youtube.com/watch?v=1Gf34Z7aops&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=61
    //https://www.youtube.com/watch?v=q9uLnd9Zqbg&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=62

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








    private void addTimes() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);

        alertDialog.setTitle("Provide Reservation Time");

        alertDialog.setMessage("Please provide reservation time");

        LayoutInflater inflator = this.getLayoutInflater();

        View add_time_layout = inflator.inflate(R.layout.add_new_time_layout,null);


        // edtName = add_restaurant_layout.findViewById(R.id.edtName);

        edtChooseTime=add_time_layout.findViewById(R.id.edtChooseTime);
        edtRestaurantId=add_time_layout.findViewById(R.id.restaurant_id);

        //
        edtChooseDate=add_time_layout.findViewById(R.id.edtChooseDate);
        edtChoosenTime=add_time_layout.findViewById(R.id.edtChoosenTime);


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

                datePickerDialog = new DatePickerDialog(getActivity(),R.style.datePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edtChooseDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        int mMonth=month+1;
                        String formattedMonth=""+mMonth;
                        String formattedDayOfMonth=""+dayOfMonth;

                        if(mMonth<10){

                            formattedMonth="0"+mMonth;
                        }

                        if(dayOfMonth<10){

                            formattedDayOfMonth="0"+dayOfMonth;
                        }



                        dateChoosen=String.valueOf(year+"-"+formattedMonth+"-"+formattedDayOfMonth);
                        dateTime=String.valueOf(dateChoosen+"-"+edtChoosenTime.getText().toString());


                        newReservationSlot = new ReservationSlot(edtChooseDate.getText().toString(), edtChooseTime.getText().toString(),edtRestaurantId.getText().toString(),dateRestaurantId,dateTime,peopleCount);
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

                timePickerDialog = new TimePickerDialog(getActivity(),R.style.timePickerDialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        //edtChooseTime.setText(hourOfDay + ":" + minute);

                        edtChoosenTime.setText(String.format("%02d-%02d", hourOfDay, minute));

                        edtChooseTime.setText(String.format("%02d:%02d", hourOfDay, minute));

                        dateRestaurantId= dateChoosen+ edtRestaurantId.getText().toString();






                        dateTime=String.valueOf(dateChoosen+"-"+edtChoosenTime.getText().toString());

                        //timeInt=hourOfDay



                        // newReservationSlot = new ReservationSlot(edtChooseTime.getText().toString(),edtRestaurantId.getText().toString());


                        newReservationSlot = new ReservationSlot(edtChooseDate.getText().toString(),edtChooseTime.getText().toString(),edtRestaurantId.getText().toString(),dateRestaurantId,dateTime,peopleCount);




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
        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();

                if(newReservationSlot!=null)
                {
                    reservationSlot.push().setValue(new ReservationSlot(edtChooseDate.getText().toString(), edtChooseTime.getText().toString(),edtRestaurantId.getText().toString(),dateRestaurantId,dateTime,peopleCount));


                    Toast.makeText(getActivity(), "Time slot created", Toast.LENGTH_SHORT).show();

                }




            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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



                restaurant_address.setText(currentRestaurant.getAddress());







                restaurant_description.setText(currentRestaurant.getDescription());

                restaurant_phoneNumber.setText(currentRestaurant.getPhoneNumber());

                txtLocalEthos.setText(currentRestaurant.getLocalEthos());


                txtHours.setText(currentRestaurant.getHours());





            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

    }

}
