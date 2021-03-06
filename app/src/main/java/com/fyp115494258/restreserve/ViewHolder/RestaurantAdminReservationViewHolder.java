package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;

public class RestaurantAdminReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtPersonName,txtPersonPhoneNumber,txtDate,txtTime,txtNumberOfPeople;

    private ItemClickListener itemClickListener;


    public RestaurantAdminReservationViewHolder(@NonNull View itemView) {
        super(itemView);

        txtPersonName = itemView.findViewById(R.id.txtPersonName);
        txtPersonPhoneNumber = itemView.findViewById(R.id.txtPersonPhoneNumber);
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

