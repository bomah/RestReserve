package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;

public class TimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


   //Referred to the following : https://stackoverflow.com/questions/41223413/how-to-hide-an-item-from-recycler-view-on-a-particular-condition
   // To hide the layout
    public Button btnTime;


    public LinearLayout llTime;

    public LinearLayout.LayoutParams params;



    private ItemClickListener itemClickListener;

    public TimeViewHolder(@NonNull View itemView) {
        super(itemView);

        params=new LinearLayout.LayoutParams(0,0);
        llTime=itemView.findViewById(R.id.llTimeSlot);

        btnTime=(Button)itemView.findViewById(R.id.btnTimeSlot);


        btnTime.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

}
