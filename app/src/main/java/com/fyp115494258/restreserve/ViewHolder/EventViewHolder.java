package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;


public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView txtEventName;
    public ImageView imgEvent;

    public TextView txtEventDescription;




    public EventViewHolder(@NonNull View itemView) {
       super(itemView);

        txtEventName = itemView.findViewById(R.id.txtEventName);


        txtEventDescription = itemView.findViewById(R.id.txtEventDescription);
        imgEvent=itemView.findViewById((R.id.imgEvent));





        }

    }



