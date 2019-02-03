package com.fyp115494258.restreserve;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Model.Rating;
import com.fyp115494258.restreserve.ViewHolder.ShowCommentViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ShowComment extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingTbl;

    SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseRecyclerAdapter<Rating,ShowCommentViewHolder> adapter;

    String restaurantId="";

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter !=null)
            adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);


        database=FirebaseDatabase.getInstance();
        ratingTbl=database.getReference("Rating");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerComment);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(getIntent() !=null)
                    restaurantId=getIntent().getStringExtra(Common.INTENT_RESTAURANT_ID);

                if(!restaurantId.isEmpty() && restaurantId !=null)
                {
                    Query query = ratingTbl.orderByChild("restaurantId").equalTo(restaurantId);


                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();


                    adapter=new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {

                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserPhone.setText(model.getUserPhone());
                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout,parent,false);

                            return new ShowCommentViewHolder(view);
                        }
                    };

                    loadComment(restaurantId);
                        }
                    }




        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);


                if(getIntent() !=null)
                    restaurantId=getIntent().getStringExtra(Common.INTENT_RESTAURANT_ID);

                if(!restaurantId.isEmpty() && restaurantId !=null)
                {
                    Query query = ratingTbl.orderByChild("restaurantId").equalTo(restaurantId);


                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();


                    adapter=new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {

                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserPhone.setText(model.getUserPhone());
                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout,parent,false);

                            return new ShowCommentViewHolder(view);
                        }
                    };

                    loadComment(restaurantId);
                }


            }
        });
    }


    private void loadComment(String restaurantId) {

        adapter.startListening();

        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
