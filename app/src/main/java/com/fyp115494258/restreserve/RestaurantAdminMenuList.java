package com.fyp115494258.restreserve;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;


import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp115494258.restreserve.Model.FoodMenuItem;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.MainCourse;
import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.Model.Starter;

import com.fyp115494258.restreserve.ViewHolder.MainCourseViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminMainCourseViewHolder;
import com.fyp115494258.restreserve.ViewHolder.RestaurantAdminStartersViewHolder;
import com.fyp115494258.restreserve.ViewHolder.StartersViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class RestaurantAdminMenuList extends Fragment  {

    RecyclerView recyclerViewMains;
    RecyclerView recyclerViewStarters;



    RecyclerView.LayoutManager layoutManagerMains;
    RecyclerView.LayoutManager layoutManagerStarters;

    FirebaseDatabase database;
    DatabaseReference mainsTbl;
    DatabaseReference startersTbl;



    FirebaseRecyclerAdapter<FoodMenuItem, RestaurantAdminMainCourseViewHolder> mainCourseAdapter;

    FirebaseRecyclerAdapter<FoodMenuItem, RestaurantAdminStartersViewHolder> startersAdapter;

    String restaurantId = "";

    EditText edtFoodName;
    EditText edtPrice;




    View RestaurantAdminMenuListView;

    public RestaurantAdminMenuList(){

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
        RestaurantAdminMenuListView= inflater.inflate(R.layout.fragment_restaurant_admin_menu_list, container, false);


        database = FirebaseDatabase.getInstance();
        mainsTbl = database.getReference("MainCourse");
        startersTbl=database.getReference("Starters");

        recyclerViewMains=(RecyclerView)RestaurantAdminMenuListView.findViewById(R.id.recyclerMainCourse);
        recyclerViewStarters=(RecyclerView)RestaurantAdminMenuListView.findViewById(R.id.recyclerStarters);

        layoutManagerMains = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        layoutManagerStarters = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);


        recyclerViewMains.setLayoutManager(layoutManagerMains);
        recyclerViewStarters.setLayoutManager(layoutManagerStarters);





        if(Common.restId !=null) {
            restaurantId = Common.restId;

            loadStarters(restaurantId);
            loadMainCourse(restaurantId);

        }
        
        return RestaurantAdminMenuListView;
        
        
        
        
    }

    private void loadStarters(String restaurantId) {

        Query query = startersTbl.orderByChild("restaurantId").equalTo(restaurantId);


        FirebaseRecyclerOptions<FoodMenuItem> options = new FirebaseRecyclerOptions.Builder<FoodMenuItem>()
                .setQuery(query,FoodMenuItem.class)
                .build();


        startersAdapter=new FirebaseRecyclerAdapter<FoodMenuItem, RestaurantAdminStartersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantAdminStartersViewHolder holder, int position, @NonNull FoodMenuItem model) {


                holder.txtFoodName.setText(model.getFoodName());
                holder.txtPrice.setText(model.getPrice());
            }

            @NonNull
            @Override
            public RestaurantAdminStartersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.starters_layout,parent,false);

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
            protected void onBindViewHolder(@NonNull RestaurantAdminMainCourseViewHolder holder, int position, @NonNull FoodMenuItem model) {


                holder.txtFoodName.setText(model.getFoodName());
                holder.txtPrice.setText(model.getPrice());
            }

            @NonNull
            @Override
            public RestaurantAdminMainCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.maincourse_layout,parent,false);

                return new RestaurantAdminMainCourseViewHolder(view);
            }
        };

        mainCourseAdapter.startListening();

        recyclerViewMains.setAdapter(mainCourseAdapter);



    }





    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateStartersDialog(startersAdapter.getRef(item.getOrder()).getKey(),startersAdapter.getItem(item.getOrder()));

            showUpdateMainCourseDialog(mainCourseAdapter.getRef(item.getOrder()).getKey(),mainCourseAdapter.getItem(item.getOrder()));

        }
        else if (item.getTitle().equals(Common.DELETE))
        {

            deleteStarter(startersAdapter.getRef(item.getOrder()).getKey());


            deleteMainCourse(startersAdapter.getRef(item.getOrder()).getKey());

        }




        return super.onContextItemSelected(item);
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
