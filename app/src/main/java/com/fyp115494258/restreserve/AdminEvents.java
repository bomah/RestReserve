package com.fyp115494258.restreserve;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.Event;
import com.fyp115494258.restreserve.Model.FoodMenuItem;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.AdminEventViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AdminEvents extends AppCompatActivity {

    RecyclerView recyclerViewEvent;
    RecyclerView recyclerViewUpcomingEvent;



    RecyclerView.LayoutManager layoutManagerEvent;
    RecyclerView.LayoutManager layoutManagerUpcomingEvent;

    FirebaseDatabase database;
    DatabaseReference eventTbl;
    DatabaseReference upcomingEventTbl;



    FirebaseRecyclerAdapter<Event, AdminEventViewHolder> eventAdapter;

    FirebaseRecyclerAdapter<Event, AdminEventViewHolder> upcomingEventAdapter;







    Event newEvent;
    Event newUpcomingEvent;


    FirebaseStorage storage;
    StorageReference storageReference;


    Uri saveUri;
    private final int PICK_IMAGE_REQUEST=71;






    EditText edtAddEventName;
    EditText edtAddDescription;


    EditText edtUpdateEventName;
    EditText edtUpdateDescription;

    Button btnSelect;
    Button btnUpload;




    ImageButton btnAddEvent;

    ImageButton btnAddUpcomingEvent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_events);


        database = FirebaseDatabase.getInstance();



        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        eventTbl = database.getReference("Event");


        upcomingEventTbl = database.getReference("UpcomingEvent");


        recyclerViewEvent=(RecyclerView)findViewById(R.id.recyclerEvents);
        recyclerViewUpcomingEvent=(RecyclerView)findViewById(R.id.recyclerUpcomingEvents);


        btnAddEvent=(ImageButton)findViewById(R.id.btnAddEvent);
        btnAddUpcomingEvent=(ImageButton)findViewById(R.id.btnAddUpcomingEvent);

        layoutManagerEvent = new LinearLayoutManager(AdminEvents.this,LinearLayoutManager.VERTICAL,false);
        layoutManagerUpcomingEvent = new LinearLayoutManager(AdminEvents.this,LinearLayoutManager.VERTICAL,false);


        recyclerViewEvent.setLayoutManager(layoutManagerEvent);
        recyclerViewUpcomingEvent.setLayoutManager(layoutManagerUpcomingEvent);



        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        btnAddUpcomingEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpcomingEvent();
            }
        });



        loadEvent();

        loadUpcomingEvent();


    }


    private void addEvent() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminEvents.this);

        alertDialog.setTitle("Add new event");

        alertDialog.setMessage("Please provide details");

        LayoutInflater inflator = this.getLayoutInflater();

        View add_new_event = inflator.inflate(R.layout.add_new_event,null);




        edtAddEventName = add_new_event.findViewById(R.id.edtAddEventName);
        edtAddDescription = add_new_event.findViewById(R.id.edtAddDescription);




        btnSelect = add_new_event.findViewById(R.id.btnSelect);
        btnUpload = add_new_event.findViewById(R.id.btnUpload);


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

                uploadImage();
            }
        });




        alertDialog.setView(add_new_event);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();






                eventTbl.push().setValue(newEvent);
                Toast.makeText(AdminEvents.this, "New Event Added ", Toast.LENGTH_SHORT).show();



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

    private void uploadImage() {
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
                            Toast.makeText(AdminEvents.this, "Uploaded ", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {



                                    newEvent = new Event(edtAddEventName.getText().toString(),edtAddDescription.getText().toString(),uri.toString());

                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AdminEvents.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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











    private void addUpcomingEvent() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminEvents.this);

        alertDialog.setTitle("Add new upcoming event");

        alertDialog.setMessage("Please provide details");

        LayoutInflater inflator = this.getLayoutInflater();

        View add_new_event = inflator.inflate(R.layout.add_new_event,null);




        edtAddEventName = add_new_event.findViewById(R.id.edtAddEventName);
        edtAddDescription = add_new_event.findViewById(R.id.edtAddDescription);



        btnSelect = add_new_event.findViewById(R.id.btnSelect);
        btnUpload = add_new_event.findViewById(R.id.btnUpload);



        //newStarter= new FoodMenuItem(edtAddFoodName.getText().toString(),edtAddPrice.getText().toString(),Common.restId);


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

                uploadUpcomingEventImage();
            }
        });






        alertDialog.setView(add_new_event);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();




                upcomingEventTbl.push().setValue(newUpcomingEvent);
                Toast.makeText(AdminEvents.this, "New Starter Added ", Toast.LENGTH_SHORT).show();



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




    private void uploadUpcomingEventImage() {
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
                            Toast.makeText(AdminEvents.this, "Uploaded ", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {



                                    newUpcomingEvent = new Event(edtAddEventName.getText().toString(),edtAddDescription.getText().toString(),uri.toString());

                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AdminEvents.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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






    private void loadEvent() {

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(eventTbl,Event.class)
                .build();

        eventAdapter = new FirebaseRecyclerAdapter<Event, AdminEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminEventViewHolder viewHolder, int position, @NonNull Event model) {



                viewHolder.txtEventName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imgEvent);

                viewHolder.txtEventDescription.setText(model.getDescription());





                final AdminEventViewHolder mHolder = viewHolder;
                final int mPosition = position;

                viewHolder.btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu popup = new PopupMenu(v.getContext(), mHolder.btnOptions);
                        popup.getMenuInflater().inflate(R.menu.options_fooditem, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.updateFoodItem:

                                        showUpdateEventDialog(eventAdapter.getRef(mPosition).getKey(), eventAdapter.getItem(mPosition));


                                        break;
                                    case R.id.deleteFoodItem:
                                        // to delete

                                        deleteEvent(eventAdapter.getRef(mPosition).getKey());

                                        break;
                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();


                    }


                });

            }

            @NonNull
            @Override
            public AdminEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.admin_event_layout,parent,false);
                return new AdminEventViewHolder(itemView);
            }
        };
        eventAdapter.startListening();
        recyclerViewEvent.setAdapter(eventAdapter);

    }



    private void loadUpcomingEvent() {

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(upcomingEventTbl,Event.class)
                .build();

        upcomingEventAdapter = new FirebaseRecyclerAdapter<Event, AdminEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminEventViewHolder viewHolder, int position, @NonNull Event model) {


                viewHolder.txtEventName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imgEvent);

                viewHolder.txtEventDescription.setText(model.getDescription());


                final AdminEventViewHolder mHolder = viewHolder;
                final int mPosition = position;

                viewHolder.btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu popup = new PopupMenu(v.getContext(), mHolder.btnOptions);
                        popup.getMenuInflater().inflate(R.menu.options_fooditem, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.updateFoodItem:

                                        showUpdateUpcomingEventDialog(upcomingEventAdapter.getRef(mPosition).getKey(), upcomingEventAdapter.getItem(mPosition));


                                        break;
                                    case R.id.deleteFoodItem:
                                        // to delete

                                        deleteUpcomingEvent(upcomingEventAdapter.getRef(mPosition).getKey());

                                        break;
                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();


                    }


                });

            }

            @NonNull
            @Override
            public AdminEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.admin_event_layout,parent,false);
                return new AdminEventViewHolder(itemView);
            }
        };
        upcomingEventAdapter.startListening();
        recyclerViewUpcomingEvent.setAdapter(upcomingEventAdapter);

    }


    private void deleteEvent(String key) {

        eventTbl.child(key).removeValue();
        Toast.makeText(AdminEvents.this,"Event deleted",Toast.LENGTH_SHORT).show();

    }

    private void deleteUpcomingEvent(String key) {

        upcomingEventTbl.child(key).removeValue();
        Toast.makeText(AdminEvents.this,"Event deleted",Toast.LENGTH_SHORT).show();

    }






    private void showUpdateEventDialog(final String key,final Event item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminEvents.this);

        alertDialog.setTitle("Update Event");

        alertDialog.setMessage("Please provide details");

        LayoutInflater inflator = this.getLayoutInflater();

        View update_event_layout = inflator.inflate(R.layout.update_event_layout,null);


        edtUpdateEventName = update_event_layout.findViewById(R.id.edtUpdateEventName);
        edtUpdateDescription = update_event_layout.findViewById(R.id.edtUpdateDescription);




        //set default
        edtUpdateEventName.setText(item.getName());
        edtUpdateDescription.setText(item.getDescription());


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




        alertDialog.setView(update_event_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();






                //Update Information
                item.setName(edtUpdateEventName.getText().toString());

                item.setDescription(edtUpdateDescription.getText().toString());

                eventTbl.child(key).setValue(item);








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


    private void showUpdateUpcomingEventDialog(final String key,final Event item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminEvents.this);

        alertDialog.setTitle("Update Event");

        alertDialog.setMessage("Please provide details");

        LayoutInflater inflator = this.getLayoutInflater();

        View update_event_layout = inflator.inflate(R.layout.update_event_layout,null);


        edtUpdateEventName = update_event_layout.findViewById(R.id.edtUpdateEventName);
        edtUpdateDescription = update_event_layout.findViewById(R.id.edtUpdateDescription);




        //set default
        edtUpdateEventName.setText(item.getName());
        edtUpdateDescription.setText(item.getDescription());


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




        alertDialog.setView(update_event_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();






                //Update Information
                item.setName(edtUpdateEventName.getText().toString());

                item.setDescription(edtUpdateDescription.getText().toString());

                eventTbl.child(key).setValue(item);








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




    private void changeImage(final Event item) {
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
                            Toast.makeText(AdminEvents.this, "Uploaded ", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdminEvents.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

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
