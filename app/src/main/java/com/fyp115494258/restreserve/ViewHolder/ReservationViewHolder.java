package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;

public class ReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ////Referred to the following video: https://www.youtube.com/watch?v=dJm7LACOn80&list=PLaoF-xhnnrRW4lXuIhNLhgVuYkIlF852V&index=2

    public TextView txtRestaurantName,txtDate,txtTime,txtNumberOfPeople;

    private ItemClickListener itemClickListener;


    public ReservationViewHolder(@NonNull View itemView) {
        super(itemView);

        txtRestaurantName = itemView.findViewById(R.id.txtRestaurantName);

        txtDate = itemView.findViewById(R.id.txtDate);
        txtTime = itemView.findViewById(R.id.txtTime);
        txtNumberOfPeople = itemView.findViewById(R.id.txtNumberOfPeople);


        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
