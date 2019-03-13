package com.fyp115494258.restreserve;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;


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


public class RestaurantDet extends AppCompatActivity implements RatingDialogListener {

    FirebaseDatabase database;
    DatabaseReference restaurant;

    Restaurant currentRestaurant;



    ViewPager viewPager;
    TabsAdapter adapter;

    TabLayout tabLayout;

    ImageView restaurant_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    //Rating
    FloatingActionButton btnRating;
    RatingBar ratingBar;

    Toolbar tBar;

    String RestaurantId="";


    DatabaseReference ratingTbl;









    //https://www.youtube.com/watch?v=3Am-iad_Gkg


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_det);


        if(getIntent() != null)
            RestaurantId = getIntent().getStringExtra("RestaurantId");

        Common.restId=RestaurantId;



        //NestedScrollView scrollView=(NestedScrollView)findViewById(R.id.nestedScrollView);
        //scrollView.setFillViewport(true);


        //Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");

        ratingTbl=database.getReference("Rating");

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);






        tabLayout = (TabLayout) findViewById(R.id.tabLayout);



        tabLayout.setupWithViewPager(viewPager);






        restaurant_image = (ImageView) findViewById(R.id.img_restaurant);

        ratingBar=(RatingBar)findViewById(R.id.ratingBar);





        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        

        //collapsingToolbarLayout.setTitle("Title");

        tBar=(Toolbar) findViewById(R.id.tBar);
        //setSupportActionBar(tBar);
       // getSupportActionBar().setTitle("Title");



        btnRating=(FloatingActionButton)findViewById(R.id.btnRating);


        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });








        //Bundle bundle = getIntent().getExtras();




        if(!RestaurantId.isEmpty()){
            //getTimes(RestaurantId);




            getDetailRestaurant(RestaurantId);

            //getRatingRestaurant(RestaurantId);



        }








    }


    //Referred to the following
    //https://www.youtube.com/watch?v=LxOMuzV3L20&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=20
    ////https://www.youtube.com/watch?v=pld3yRFS5WY&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=21

    private void getRatingRestaurant(String restaurantId) {

        Query restaurantRating=ratingTbl.orderByChild("restaurantId").equalTo(restaurantId);

        restaurantRating.addValueEventListener(new ValueEventListener() {

            int count=0,sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                    Rating item =postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count !=0)
                {
                    float average= sum/count;
                    ratingBar.setRating(average);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



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
                .setStarColor(R.color.colorPrimary)
                .setNoteDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please comment here...")
                .setHintTextColor(R.color.white)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimary)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(RestaurantDet.this)
                .show();


    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override

    public void onPositiveButtonClicked(int value, @NotNull String comments) {


        final Rating rating = new Rating(Common.currentUser.getFirstName(),
                Common.restId,
                Common.currentRestaurant.getName(),
                String.valueOf(value),
                comments);

        ratingTbl.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(RestaurantDet.this, "Review submitted",Toast.LENGTH_SHORT).show();

                    }
                });


    }




    private void getDetailRestaurant(String restaurantId) {

       // restaurant.child(restaurantId).addValueEventListener(new ValueEventListener(){

        restaurant.child(restaurantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentRestaurant = dataSnapshot.getValue(Restaurant.class);

                Common.currentRestaurant=currentRestaurant;

                //Set Image
                Picasso.get().load(currentRestaurant.getImage()).into(restaurant_image);

                collapsingToolbarLayout.setTitle(currentRestaurant.getName());


                getRatingRestaurant(Common.restId);

                //getSupportActionBar().setTitle(currentRestaurant.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}


