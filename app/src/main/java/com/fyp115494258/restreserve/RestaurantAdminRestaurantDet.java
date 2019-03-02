package com.fyp115494258.restreserve;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.Rating;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RestaurantAdminRestaurantDet extends AppCompatActivity  {

    FirebaseDatabase database;
    DatabaseReference restaurant;

    Restaurant currentRestaurant;


    ViewPager viewPager;
    RestaurantAdminTabsAdapter adapter;

    TabLayout tabLayout;

    ImageView restaurant_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    //Rating
    FloatingActionButton btnRating;
    RatingBar ratingBar;

    Toolbar tBar;

    String RestaurantId = "";


    DatabaseReference ratingTbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_admin_restaurant_det);


        if (getIntent() != null)
            RestaurantId = getIntent().getStringExtra("RestaurantId");

        Common.restId = RestaurantId;


        //NestedScrollView scrollView=(NestedScrollView)findViewById(R.id.nestedScrollView);
        //scrollView.setFillViewport(true);


        //Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");

        ratingTbl = database.getReference("Rating");

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new RestaurantAdminTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        tabLayout.setupWithViewPager(viewPager);


        restaurant_image = (ImageView) findViewById(R.id.img_restaurant);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        //collapsingToolbarLayout.setTitle("Title");

        tBar = (Toolbar) findViewById(R.id.tBar);
        //setSupportActionBar(tBar);
        // getSupportActionBar().setTitle("Title");



        //Bundle bundle = getIntent().getExtras();


        if (!RestaurantId.isEmpty()) {
            //getTimes(RestaurantId);


            getDetailRestaurant(RestaurantId);

            getRatingRestaurant(RestaurantId);


        }


    }


    //Referred to the following
    //https://www.youtube.com/watch?v=LxOMuzV3L20&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=20

    private void getRatingRestaurant(String restaurantId) {

        Query restaurantRating = ratingTbl.orderByChild("restaurantId").equalTo(restaurantId);

        restaurantRating.addValueEventListener(new ValueEventListener() {

            int count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }







    private void getDetailRestaurant(String restaurantId) {

        // restaurant.child(restaurantId).addValueEventListener(new ValueEventListener(){

        restaurant.child(restaurantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentRestaurant = dataSnapshot.getValue(Restaurant.class);

                Common.currentRestaurant = currentRestaurant;

                //Set Image
                Picasso.get().load(currentRestaurant.getImage()).into(restaurant_image);

                collapsingToolbarLayout.setTitle(currentRestaurant.getName());

                //getSupportActionBar().setTitle(currentRestaurant.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}



