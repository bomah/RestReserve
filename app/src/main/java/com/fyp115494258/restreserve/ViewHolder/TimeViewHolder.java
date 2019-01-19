package com.fyp115494258.restreserve.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fyp115494258.restreserve.Interface.ItemClickListener;
import com.fyp115494258.restreserve.R;

public class TimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    public Button btnTime;


    private ItemClickListener itemClickListener;

    public TimeViewHolder(@NonNull View itemView) {
        super(itemView);

        btnTime=(Button)itemView.findViewById(R.id.btnTimeSlot);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

}
