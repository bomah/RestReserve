package com.fyp115494258.restreserve;

import android.app.DatePickerDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
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


    TextView restaurant_name, restaurant_location, restaurant_description,restaurant_phoneNumber;
    ImageView restaurant_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ElegantNumberButton numberButton;


    FirebaseDatabase database;
    DatabaseReference restaurant;
    Restaurant currentRestaurant;



    String RestaurantId = "";
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;

    DatabaseReference reservationSlot;

    RecyclerView recycler_time;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<ReservationSlot,TimeViewHolder> recyclerAdapter;


    //
    EditText edtChooseDate;

    DatePickerDialog datePickerDialog;

    int year;
    int month;
    int dayOfMonth;

    Calendar calendar;

    String dateChoosen;

    String currentRestId;

    String restId;

    String dateRestaurantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);


        //Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");


        reservationSlot = database.getReference("ReservationSlot");




        //Time Slot
        recycler_time=(RecyclerView)findViewById(R.id.recycler_time);
        recycler_time.setHasFixedSize(true);
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


        //
        edtChooseDate=(EditText)findViewById(R.id.edtChooseDate);

        /*
        edtChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(RestaurantDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edtChooseDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        dateChoosen = edtChooseDate.getText().toString();



                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

*/


        //Get food ID from Intent
        if(getIntent() != null)
            RestaurantId = getIntent().getStringExtra("RestaurantId");
        if(!RestaurantId.isEmpty()){
            getDetailRestaurant(RestaurantId);
            //getTimes(RestaurantId);
            currentRestId = RestaurantId;
            getDate(RestaurantId);

        }
    }

    private void getDate(final String restaurantId) {

        //reservationSlot.orderByChild("restaurantId").equalTo(restaurantId);

        edtChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(RestaurantDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edtChooseDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);



                        dateChoosen = edtChooseDate.getText().toString() + restaurantId;



                        getTimes(dateChoosen);




                    }
                }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
    }


    private void getTimes(final String dateChoosen) {


        //Query time = reservationSlot.orderByChild("restaurantId").equalTo(restaurantId);


        //reservationSlot.orderByChild("restaurantId").equalTo(currentRestId);



            recyclerAdapter = new FirebaseRecyclerAdapter<ReservationSlot, TimeViewHolder>(ReservationSlot.class,
                    R.layout.timeslot_item,
                    TimeViewHolder.class,
                    //reservationSlot.orderByChild("restaurantId").equalTo(restaurantId)

                    reservationSlot.orderByChild("dateRestaurantId").equalTo(dateChoosen) ) {
                @Override
                protected void populateViewHolder(TimeViewHolder viewHolder, ReservationSlot model, int position) {






                    viewHolder.btnTime.setText(model.getTime());


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


                                                        }
                                                    }

                    );


                }
            };
            recycler_time.setAdapter(recyclerAdapter);







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



            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

    }


}
