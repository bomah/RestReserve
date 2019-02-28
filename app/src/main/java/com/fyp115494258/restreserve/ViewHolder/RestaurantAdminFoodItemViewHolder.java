package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp115494258.restreserve.Common.Common;
import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;

public class RestaurantAdminFoodItemViewHolder extends RecyclerView.ViewHolder
       {


    public TextView txtFoodName,txtPrice;

    public ImageButton btnOptions;




    public RestaurantAdminFoodItemViewHolder(@NonNull View itemView) {
        super(itemView);

        txtFoodName = (TextView) itemView.findViewById(R.id.txtFoodName);
        txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);



        btnOptions = (ImageButton) itemView.findViewById(R.id.btnOptions);




    }





}
