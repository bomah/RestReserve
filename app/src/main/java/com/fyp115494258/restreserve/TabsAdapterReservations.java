package com.fyp115494258.restreserve;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsAdapterReservations extends FragmentStatePagerAdapter {

    //int mNoOfTabs;

    int mCurrentPosition=-1;

    public TabsAdapterReservations(FragmentManager fm)
    {
        super(fm);
        //this.mNoOfTabs=NumberOfTabs;
    }







    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                UpcomingReservations upcomingReservations= new UpcomingReservations();

                return upcomingReservations;



            case 1:


            PastReservations pastReservations =new PastReservations();
            return pastReservations;


            default:
                return null;


        }


    }








    @Override
    public int getCount() {
        return 2;
    }



    @Override    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "Upcoming";
            case 1: return "Past";

            default: return null;
        }

    }
}
