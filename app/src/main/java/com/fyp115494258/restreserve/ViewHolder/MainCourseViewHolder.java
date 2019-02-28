package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fyp115494258.restreserve.R;

public class MainCourseViewHolder extends RecyclerView.ViewHolder {

    public TextView txtFoodName,txtPrice;


    public MainCourseViewHolder(@NonNull View itemView) {
        super(itemView);

        txtFoodName=(TextView)itemView.findViewById(R.id.txtMainsFoodName);
        txtPrice=(TextView)itemView.findViewById(R.id.txtMainsPrice);
    }
}
