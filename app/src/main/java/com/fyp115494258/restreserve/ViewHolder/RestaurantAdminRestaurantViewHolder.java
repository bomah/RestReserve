package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;

public class RestaurantAdminRestaurantViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,
        View.OnCreateContextMenuListener{


    //Referred to the following video: https://www.youtube.com/watch?v=dJm7LACOn80&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=2
    //2ND ITERATION
    public TextView txtRestaurantName;
    public ImageView imageView;

    public TextView txtRestaurantDistance;

    private ItemClickListener itemClickListener;

    public RestaurantAdminRestaurantViewHolder(@NonNull View itemView) {
        super(itemView);

        txtRestaurantName=(TextView)itemView.findViewById(R.id.restaurant_name);
        imageView =(ImageView)itemView.findViewById(R.id.restaurant_image);

        txtRestaurantDistance=(TextView)itemView.findViewById(R.id.restaurant_distance);

        itemView.setOnCreateContextMenuListener(this);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        //  menu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
