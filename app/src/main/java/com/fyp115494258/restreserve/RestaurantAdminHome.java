package com.fyp115494258.restreserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.Model.ReservationSlot;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminRestaurantViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantViewHolder;
import com.fyp115494258.restreserve.ViewHolder.TimeViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class RestaurantAdminHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //2nd Iteration

    FirebaseDatabase database;
    DatabaseReference restaurant;

    FirebaseStorage storage;
    StorageReference storageReference;

    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Restaurant,RestaurantAdminRestaurantViewHolder> adapter;


    //Server side
    EditText edtName,edtDescription,edtPhoneNumber,edtAdminPhoneNumber;

    Button btnUpload,btnSelect;


    Restaurant newRestaurant;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST=71;


    DrawerLayout drawer;



    String adminPhoneNo = "";


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
        restaurant=database.getReference("Restaurant");


        //server side
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //2nd Iteration
        //Set Name for User
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getFirstName());

        //load menu
        //Using Firebase UI to bind data from Firebase to Recycler View
        recycler_menu=(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        //Get Intent here

        if (getIntent() != null)
            adminPhoneNo = getIntent().getStringExtra("AdminPhoneNo");
        if (!adminPhoneNo.isEmpty() && adminPhoneNo != null) {
            getRestaurant(adminPhoneNo);
        }



    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    private void SendUserToLogIn() {

        Intent logInIntent = new Intent(RestaurantAdminHome.this,LogIn.class);
        logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logInIntent);
        finish();

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() !=null)
        {
            saveUri=data.getData();
            btnSelect.setText("Image Selected");

        }
    }


    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }







    private void getRestaurant(String adminPhoneNo) {

        Query getRestaurant = restaurant.orderByChild("adminPhoneNumber")
                .equalTo(adminPhoneNo);










        FirebaseRecyclerOptions<Restaurant> options = new FirebaseRecyclerOptions.Builder<Restaurant>()
                .setQuery(getRestaurant,Restaurant.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Restaurant,RestaurantAdminRestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantAdminRestaurantViewHolder viewHolder, int position, @NonNull Restaurant model) {






                viewHolder.txtRestaurantName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });


                final Restaurant clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                                                    @Override
                                                    public void onClick(View view, int position, boolean isLongClick) {
                                                        //Toast.makeText(Home.this, ""+clickItem.getName(),Toast.LENGTH_SHORT).show();



                                                        Intent restaurantDetail = new Intent(RestaurantAdminHome.this, RestaurantAdminRestaurantDetail.class);
                                                        restaurantDetail.putExtra("RestaurantId", adapter.getRef(position).getKey());
                                                        startActivity(restaurantDetail);

                                                    }
                                                }

                );


            }
            @NonNull
            @Override
            public RestaurantAdminRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item, parent, false);
                return new RestaurantAdminRestaurantViewHolder(itemView);
            }


        };
        adapter.startListening();

        recycler_menu.setAdapter(adapter);

    }





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
        }else if (id == R.id.nav_reservations) {
            Intent reservationList = new Intent(RestaurantAdminHome.this, RestaurantAdminReservationList.class);
            startActivity(reservationList);

        }


        else if (id == R.id.nav_logout) {

            mAuth.signOut();
            SendUserToLogIn();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));




        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(final String key, final Restaurant item) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantAdminHome.this);

        alertDialog.setTitle("Update Restaurant Details");

        alertDialog.setMessage("Please provide information");

        LayoutInflater inflator = this.getLayoutInflater();

        View add_restaurant_layout = inflator.inflate(R.layout.update_restaurant_layout,null);


        edtName = add_restaurant_layout.findViewById(R.id.edtName);
        edtDescription = add_restaurant_layout.findViewById(R.id.edtDescription);

        edtPhoneNumber= add_restaurant_layout.findViewById(R.id.edtPhoneNumber);
        //
        edtAdminPhoneNumber= add_restaurant_layout.findViewById(R.id.edtAdminPhoneNumber);
        btnSelect = add_restaurant_layout.findViewById(R.id.btnSelect);
        btnUpload = add_restaurant_layout.findViewById(R.id.btnUpload);


        //set default name
        edtName.setText(item.getName());
        edtAdminPhoneNumber.setText(item.getAdminPhoneNumber());


        edtPhoneNumber.setText(item.getPhoneNumber());
        edtDescription.setText(item.getDescription());

        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //Let user select image from Gallery and save Uri of this image
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                changeImage(item);
            }
        });

        alertDialog.setView(add_restaurant_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();






                //Update Information
                item.setName(edtName.getText().toString());

                item.setDescription(edtDescription.getText().toString());
                item.setPhoneNumber(edtPhoneNumber.getText().toString());
                item.setAdminPhoneNumber(edtAdminPhoneNumber.getText().toString());
                restaurant.child(key).setValue(item);








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

    private void changeImage(final Restaurant item) {

        if(saveUri != null){

            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading..");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText(RestaurantAdminHome.this, "Uploaded ", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    item.setImage(uri.toString());
                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(RestaurantAdminHome.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress =(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded" + progress +"%");
                        }
                    });

        }

    }


}
