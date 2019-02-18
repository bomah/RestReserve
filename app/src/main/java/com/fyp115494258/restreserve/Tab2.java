package com.fyp115494258.restreserve;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Tab2 extends Fragment {

    View Tab2View;

    public Tab2(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Tab2View= inflater.inflate(R.layout.fragment_tab2, container, false);


        return Tab2View;
    }


}

