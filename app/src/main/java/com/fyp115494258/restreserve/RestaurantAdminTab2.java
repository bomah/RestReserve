package com.fyp115494258.restreserve;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.fyp115494258.restreserve.Model.FoodMenuItem;
import com.fyp115494258.restreserve.Model.ReservationSlot;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminFoodItemViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminMainCourseViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminStartersViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantAdminTab2 extends Fragment {




    RecyclerView recyclerViewMains;
    RecyclerView recyclerViewStarters;



    RecyclerView.LayoutManager layoutManagerMains;
    RecyclerView.LayoutManager layoutManagerStarters;

    FirebaseDatabase database;
    DatabaseReference mainsTbl;
    DatabaseReference startersTbl;

    FoodMenuItem newStarter;
    FoodMenuItem newMainCourse;



    FirebaseRecyclerAdapter<FoodMenuItem, RestaurantAdminMainCourseViewHolder> mainCourseAdapter;

    FirebaseRecyclerAdapter<FoodMenuItem, RestaurantAdminStartersViewHolder> startersAdapter;

    String restaurantId = "";

    EditText edtFoodName;
    EditText edtPrice;


    EditText edtAddFoodName;
    EditText edtAddPrice;


    ImageButton btnAddStarter;

    ImageButton btnAddMainCourse;



    View RestaurantAdminTab2View;

    public RestaurantAdminTab2(){

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainCourseAdapter != null)
            mainCourseAdapter.stopListening();

        if(startersAdapter != null)
            startersAdapter.stopListening();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RestaurantAdminTab2View= inflater.inflate(R.layout.fragment_restaurant_admin_tab2, container, false);


        database = FirebaseDatabase.getInstance();
        mainsTbl = database.getReference("MainCourse");
        startersTbl=database.getReference("Starters");

        recyclerViewMains=(RecyclerView)RestaurantAdminTab2View.findViewById(R.id.recyclerMainCourse);
        recyclerViewStarters=(RecyclerView)RestaurantAdminTab2View.findViewById(R.id.recyclerStarters);



        btnAddStarter=(ImageButton)RestaurantAdminTab2View.findViewById(R.id.btnAddStarter);
        btnAddMainCourse=(ImageButton)RestaurantAdminTab2View.findViewById(R.id.btnAddMainCourse);

        layoutManagerMains = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        layoutManagerStarters = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);



        //recyclerViewMains.setHasFixedSize(true);
        //recyclerViewMains.setNestedScrollingEnabled(false);

        //recyclerViewStarters.setHasFixedSize(true);
       // recyclerViewStarters.setNestedScrollingEnabled(false);

        recyclerViewMains.setLayoutManager(layoutManagerMains);
        recyclerViewStarters.setLayoutManager(layoutManagerStarters);



        btnAddStarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStarter();
            }
        });

        btnAddMainCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMainCourse();
            }
        });







        if(Common.restId !=null) {
            restaurantId = Common.restId;

            loadStarters(restaurantId);
            loadMainCourse(restaurantId);

        }

        return RestaurantAdminTab2View;




    }

    private void addMainCourse() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Add new restaurant");

        alertDialog.setMessage("Please provide information");

        LayoutInflater inflator = this.getLayoutInflater();

        View add_fooditem_layout = inflator.inflate(R.layout.add_fooditem_layout,null);




        edtAddFoodName = add_fooditem_layout.findViewById(R.id.edtAddFoodName);
        edtAddPrice = add_fooditem_layout.findViewById(R.id.edtAddPrice);




       // newMainCourse= new FoodMenuItem(edtAddFoodName.getText().toString(),edtAddPrice.getText().toString(),Common.restId);


        //




        alertDialog.setView(add_fooditem_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();





                mainsTbl.push().setValue(new FoodMenuItem(edtAddFoodName.getText().toString(),edtAddPrice.getText().toString(),Common.restId));
                Toast.makeText(getActivity(), "New Main Course Added ", Toast.LENGTH_SHORT).show();



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

    private void addStarter() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Add new restaurant");

        alertDialog.setMessage("Please provide information");

        LayoutInflater inflator = this.getLayoutInflater();

        View add_fooditem_layout = inflator.inflate(R.layout.add_fooditem_layout,null);




        edtAddFoodName = add_fooditem_layout.findViewById(R.id.edtAddFoodName);
        edtAddPrice = add_fooditem_layout.findViewById(R.id.edtAddPrice);




        //newStarter= new FoodMenuItem(edtAddFoodName.getText().toString(),edtAddPrice.getText().toString(),Common.restId);







        alertDialog.setView(add_fooditem_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();




                startersTbl.push().setValue(new FoodMenuItem(edtAddFoodName.getText().toString(),edtAddPrice.getText().toString(),Common.restId));
                Toast.makeText(getActivity(), "New Starter Added ", Toast.LENGTH_SHORT).show();



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


    //https://www.simplifiedcoding.net/create-options-menu-recyclerview-item-tutorial/

    private void loadStarters(String restaurantId) {

        Query query = startersTbl.orderByChild("restaurantId").equalTo(restaurantId);


        FirebaseRecyclerOptions<FoodMenuItem> options = new FirebaseRecyclerOptions.Builder<FoodMenuItem>()
                .setQuery(query,FoodMenuItem.class)
                .build();


        startersAdapter=new FirebaseRecyclerAdapter<FoodMenuItem, RestaurantAdminStartersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantAdminStartersViewHolder holder, int position, @NonNull final FoodMenuItem model) {


                holder.txtFoodName.setText(model.getFoodName());
                holder.txtPrice.setText(model.getPrice());

                final RestaurantAdminStartersViewHolder mHolder=holder;
                final int mPosition=position;

                holder.btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu popup = new PopupMenu(v.getContext(), mHolder.btnOptions);
                        popup.getMenuInflater().inflate(R.menu.options_fooditem, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.updateFoodItem:

                                        showUpdateStartersDialog(startersAdapter.getRef(mPosition).getKey(),startersAdapter.getItem(mPosition));



                                        break;
                                    case R.id.deleteFoodItem:
                                        // to delete

                                        deleteStarter(startersAdapter.getRef(mPosition).getKey());

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
            public RestaurantAdminStartersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_admin_fooditem_layout,parent,false);

                return new RestaurantAdminStartersViewHolder(view);
            }
        };

        startersAdapter.startListening();

        recyclerViewStarters.setAdapter(startersAdapter);



    }


    private void loadMainCourse(String restaurantId) {

        Query query = mainsTbl.orderByChild("restaurantId").equalTo(restaurantId);


        FirebaseRecyclerOptions<FoodMenuItem> options = new FirebaseRecyclerOptions.Builder<FoodMenuItem>()
                .setQuery(query,FoodMenuItem.class)
                .build();


        mainCourseAdapter=new FirebaseRecyclerAdapter<FoodMenuItem, RestaurantAdminMainCourseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantAdminMainCourseViewHolder holder,int position, @NonNull FoodMenuItem model) {


                holder.txtFoodName.setText(model.getFoodName());
                holder.txtPrice.setText(model.getPrice());

                final RestaurantAdminMainCourseViewHolder mHolder=holder;
                final int mPosition=position;

                //https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item


                holder.btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu popup = new PopupMenu(v.getContext(), mHolder.btnOptions);
                        popup.getMenuInflater().inflate(R.menu.options_fooditem, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.updateFoodItem:

                                        showUpdateMainCourseDialog(mainCourseAdapter.getRef(mPosition).getKey(), mainCourseAdapter.getItem(mPosition));


                                        break;
                                    case R.id.deleteFoodItem:
                                        // to delete

                                        deleteMainCourse(mainCourseAdapter.getRef(mPosition).getKey());

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
            public RestaurantAdminMainCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_admin_fooditem_layout,parent,false);

                return new RestaurantAdminMainCourseViewHolder(view);
            }
        };

        mainCourseAdapter.startListening();

        recyclerViewMains.setAdapter(mainCourseAdapter);



    }













    private void deleteStarter(String key) {

        startersTbl.child(key).removeValue();
        Toast.makeText(getActivity(),"Starter deleted",Toast.LENGTH_SHORT).show();

    }

    private void deleteMainCourse(String key) {

        mainsTbl.child(key).removeValue();
        Toast.makeText(getActivity(),"Restaurant deleted",Toast.LENGTH_SHORT).show();

    }






    private void showUpdateMainCourseDialog(final String key,final FoodMenuItem item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Update Menu Item");

        alertDialog.setMessage("Please provide information");

        LayoutInflater inflator = this.getLayoutInflater();

        View update_menulist_layout = inflator.inflate(R.layout.update_menulist_layout,null);


        edtFoodName = update_menulist_layout.findViewById(R.id.edtFoodName);
        edtPrice = update_menulist_layout.findViewById(R.id.edtPrice);




        //set default
        edtFoodName.setText(item.getFoodName());
        edtPrice.setText(item.getPrice());




        alertDialog.setView(update_menulist_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();






                //Update Information
                item.setFoodName(edtFoodName.getText().toString());

                item.setPrice(edtPrice.getText().toString());

                mainsTbl.child(key).setValue(item);








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

    private void showUpdateStartersDialog(final String key, final FoodMenuItem item) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Update Menu Item");

        alertDialog.setMessage("Please provide information");

        LayoutInflater inflator = this.getLayoutInflater();

        View update_menulist_layout = inflator.inflate(R.layout.update_menulist_layout,null);


        edtFoodName = update_menulist_layout.findViewById(R.id.edtFoodName);
        edtPrice = update_menulist_layout.findViewById(R.id.edtPrice);




        //set default
        edtFoodName.setText(item.getFoodName());
        edtPrice.setText(item.getPrice());




        alertDialog.setView(update_menulist_layout);
        alertDialog.setIcon(R.drawable.ic_add_black_24dp);

        //Set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                dialogInterface.dismiss();






                //Update Information
                item.setFoodName(edtFoodName.getText().toString());

                item.setPrice(edtPrice.getText().toString());

                startersTbl.child(key).setValue(item);








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

}


