package com.fyp115494258.restreserve;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class RestaurantAdminTabsAdapter extends FragmentStatePagerAdapter {


    ////https://www.youtube.com/watch?v=3Am-iad_Gkg


    int mCurrentPosition=-1;

    public RestaurantAdminTabsAdapter(FragmentManager fm)
    {
        super(fm);
        //this.mNoOfTabs=NumberOfTabs;
    }







    @Override
    public Fragment getItem(int position) {
        switch (position){
            /*
            case 0:
                Tab1 tab1= new Tab1();

                return tab1;
            case 1:
                Tab2 tab2 =new Tab2();
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                return tab3;
                */
            case 0:
                RestaurantAdminTab1 tab1= new RestaurantAdminTab1();

                return tab1;
            case 1:
                RestaurantAdminTab2 tab2 =new RestaurantAdminTab2();
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                return tab3;

            default:
                return null;


        }


    }








    @Override
    public int getCount() {
        return 3;
    }



    @Override    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: return "Book";
            case 1: return "Menu";
            case 2: return "Reviews";
            default: return null;
        }

    }
}
