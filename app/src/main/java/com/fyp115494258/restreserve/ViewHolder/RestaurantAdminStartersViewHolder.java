package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;

public class RestaurantAdminStartersViewHolder extends RecyclerView.ViewHolder
        {

    public TextView txtFoodName,txtPrice;

    public ImageButton btnOptions;




    public RestaurantAdminStartersViewHolder(@NonNull View itemView) {
        super(itemView);

        txtFoodName=(TextView)itemView.findViewById(R.id.txtFoodName);
        txtPrice=(TextView)itemView.findViewById(R.id.txtPrice);

        btnOptions = (ImageButton) itemView.findViewById(R.id.btnOptions);


    }


}
