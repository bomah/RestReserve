package com.fyp115494258.restreserve;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //2nd Iteration


    //Referred to the following video: https://www.youtube.com/watch?v=k1RUOexThGs&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=3

    FirebaseDatabase database;
    DatabaseReference restaurant;

    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;


    //Location
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    Location mLastLocation;

    Location currentLocation, distanceLocation;
    double distance = 0;



    FirebaseAuth mAuth;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mAuth=FirebaseAuth.getInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setTitle("Menu");
        //setSupportActionBar(toolbar);

        //2nd iteration
        //Init Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurant");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //2nd Iteration
        //Set Name for User
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getFirstName());

        //load menu
        //Using Firebase UI to bind data from Firebase to Recycler View
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        //Referred to the following
        //https://www.youtube.com/watch?v=m-_9YM_aHnM&index=58&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, Common.REQUEST_CODE);

        } else {
            //buildLocationRequest();
            //buildLocationCallback();

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setSmallestDisplacement(10f);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);

            locationCallback = new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    mLastLocation = locationResult.getLastLocation();
                  /*  Toast.makeText(Home.this, new StringBuilder("")
                            .append(mLastLocation.getLatitude())
                            .append("/")
                            .append(mLastLocation.getLongitude())
                            .toString(), Toast.LENGTH_SHORT).show();
*/
                    loadRestaurant();
                }
            };

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        }






    }


    @Override
    protected void onStart() {
        super.onStart();


    }



    private void SendUserToLogIn() {

        Intent logInIntent = new Intent(Home.this,LogIn.class);
        logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logInIntent);
        finish();

    }

    private void buildLocationCallback() {

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                mLastLocation = locationResult.getLastLocation();
                Toast.makeText(Home.this, new StringBuilder("")
                        .append(mLastLocation.getLatitude())
                        .append("/")
                        .append(mLastLocation.getLongitude())
                        .toString(), Toast.LENGTH_SHORT).show();

                loadRestaurant();
            }
        };
    }

    private void buildLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);


    }

    @Override
    protected void onStop() {
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case Common.REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildLocationRequest();
                    buildLocationCallback();

                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                }
            }
        }



    }







    private void loadRestaurant() {

        FirebaseRecyclerOptions<Restaurant> options = new FirebaseRecyclerOptions.Builder<Restaurant>()
                .setQuery(restaurant,Restaurant.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder viewHolder, int position, @NonNull Restaurant model) {


                viewHolder.txtRestaurantName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);

                //referred to the following
//https://stackoverflow.com/questions/8049612/calculating-distance-between-two-geographic-locations


                currentLocation= new Location("");
                currentLocation.setLatitude(mLastLocation.getLatitude());
                currentLocation.setLongitude(mLastLocation.getLongitude());


                distanceLocation = new Location("");
                distanceLocation.setLatitude(model.getLat());
                distanceLocation.setLongitude(model.getLng());

                distance = currentLocation.distanceTo(distanceLocation)/1000;

                viewHolder.txtRestaurantDistance.setText(String.format("%.2f",distance).concat(" km"));


//https://stackoverflow.com/questions/14394366/find-distance-between-two-points-on-map-using-google-map-api-v2
/*
                int Radius = 6371;// radius of earth in Km
                double lat1 = mLastLocation.getLatitude();
                double lat2 = model.getLat();
                double lon1 = mLastLocation.getLongitude();
                double lon2 = model.getLng();
                double dLat = Math.toRadians(lat2 - lat1);
                double dLon = Math.toRadians(lon2 - lon1);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);
                double c = 2 * Math.asin(Math.sqrt(a));
                double valueResult = Radius * c;
                double km = valueResult / 1;



                viewHolder.txtRestaurantDistance.setText(String.valueOf(km));

*/





                final Restaurant clickItem=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                                                    @Override
                                                    public void onClick(View view, int position, boolean isLongClick) {
                                                        //Toast.makeText(Home.this, ""+clickItem.getName(),Toast.LENGTH_SHORT).show();

                                                       // Intent restaurantDetail=new Intent(Home.this,RestaurantDetail.class);
                                                       //restaurantDetail.putExtra("RestaurantId",adapter.getRef(position).getKey());
                                                        //startActivity(restaurantDetail);



                                                        //TabsAdapter tabAdapter=new TabsAdapter(getSupportFragmentManager());

                                                      // Bundle bundle=new Bundle();
                                                      // bundle.putString("RestaurantId",adapter.getRef(position).getKey());

                                                        //Tab1 tab1=new Tab1();
                                                       // tab1.setArguments(bundle);

                                                      // getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,tab1).commit();


                                                       Intent restaurantDet =new Intent(Home.this,RestaurantDet.class);
                                                        restaurantDet.putExtra("RestaurantId",adapter.getRef(position).getKey());
                                                       startActivity(restaurantDet);




/*
                                                        Bundle bundle=new Bundle();
                                                        bundle.putString("RestId",adapter.getRef(position).getKey());


                                                        Tab1 tab1=new Tab1();
                                                        tab1.setArguments(bundle);


                                                        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,tab1).commit();

*/




                                                    }
                                                }

                );
            }

            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item,parent,false);
                return new RestaurantViewHolder(itemView);
            }
        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);

    }


/*
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapter.startListening();
    }

 */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            return true;
        } else if (id == R.id.nav_reservations) {
            Intent reservationList = new Intent(Home.this, ReservationList.class);
            startActivity(reservationList);

        }
        else if (id == R.id.nav_logout) {


            SendUserToLogIn();
            mAuth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
