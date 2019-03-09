package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp115494258.restreserve.R;

public class AdminEventViewHolder extends RecyclerView.ViewHolder {


    public TextView txtEventName,txtEventDescription;

    public ImageView imgEvent;

    public ImageButton btnOptions;


    public AdminEventViewHolder(@NonNull View itemView) {
        super(itemView);

        txtEventName = itemView.findViewById(R.id.txtEventName);

        imgEvent=itemView.findViewById((R.id.imgEvent));
        txtEventDescription = itemView.findViewById(R.id.txtEventDescription);

        btnOptions = (ImageButton) itemView.findViewById(R.id.btnOptions);


    }

}
