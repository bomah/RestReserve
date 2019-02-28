package com.fyp115494258.restreserve;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.FoodMenuItem;


import com.fyp115494258.restreserve.ViewHolder.MainCourseViewHolder;
import com.fyp115494258.restreserve.ViewHolder.ShowCommentViewHolder;
import com.fyp115494258.restreserve.ViewHolder.StartersViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Tab2 extends Fragment {

    RecyclerView recyclerViewMains;
    RecyclerView recyclerViewStarters;



    RecyclerView.LayoutManager layoutManagerMains;
    RecyclerView.LayoutManager layoutManagerStarters;

    FirebaseDatabase database;
    DatabaseReference mainsTbl;
    DatabaseReference startersTbl;



    FirebaseRecyclerAdapter<FoodMenuItem, MainCourseViewHolder> mainCourseAdapter;

    FirebaseRecyclerAdapter<FoodMenuItem, StartersViewHolder> startersAdapter;

    String restaurantId = "";




    View Tab2View;

    public Tab2(){

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
        // Inflate the layout for this fragment
        Tab2View= inflater.inflate(R.layout.fragment_tab2, container, false);

        database = FirebaseDatabase.getInstance();
        mainsTbl = database.getReference("MainCourse");
        startersTbl=database.getReference("Starters");

        recyclerViewMains=(RecyclerView)Tab2View.findViewById(R.id.recyclerMainCourse);
        recyclerViewStarters=(RecyclerView)Tab2View.findViewById(R.id.recyclerStarters);

        layoutManagerMains = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        layoutManagerStarters = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);


        recyclerViewMains.setLayoutManager(layoutManagerMains);
        recyclerViewStarters.setLayoutManager(layoutManagerStarters);





                if(Common.restId !=null) {
                    restaurantId = Common.restId;

                    loadStarters(restaurantId);
                    loadMainCourse(restaurantId);

                }















        return Tab2View;
    }

    private void loadStarters(String restaurantId) {

        Query query = startersTbl.orderByChild("restaurantId").equalTo(restaurantId);


        FirebaseRecyclerOptions<FoodMenuItem> options = new FirebaseRecyclerOptions.Builder<FoodMenuItem>()
                .setQuery(query,FoodMenuItem.class)
                .build();


        startersAdapter=new FirebaseRecyclerAdapter<FoodMenuItem, StartersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StartersViewHolder holder, int position, @NonNull FoodMenuItem model) {


                holder.txtFoodName.setText(model.getFoodName());
                holder.txtPrice.setText(model.getPrice());
            }

            @NonNull
            @Override
            public StartersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.starters_layout,parent,false);

                return new StartersViewHolder(view);
            }
        };

        loadStartersMenu();


    }


    private void loadMainCourse(String restaurantId) {

        Query query = mainsTbl.orderByChild("restaurantId").equalTo(restaurantId);


        FirebaseRecyclerOptions<FoodMenuItem> options = new FirebaseRecyclerOptions.Builder<FoodMenuItem>()
                .setQuery(query,FoodMenuItem.class)
                .build();


        mainCourseAdapter=new FirebaseRecyclerAdapter<FoodMenuItem, MainCourseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MainCourseViewHolder holder, int position, @NonNull FoodMenuItem model) {


                holder.txtFoodName.setText(model.getFoodName());
                holder.txtPrice.setText(model.getPrice());
            }

            @NonNull
            @Override
            public MainCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.maincourse_layout,parent,false);

                return new MainCourseViewHolder(view);
            }
        };

        loadMainCourseMenu();


    }

    private void loadStartersMenu() {

        startersAdapter.startListening();

        recyclerViewStarters.setAdapter(startersAdapter);



    }

    private void loadMainCourseMenu() {

        mainCourseAdapter.startListening();

        recyclerViewMains.setAdapter(mainCourseAdapter);



    }

}

