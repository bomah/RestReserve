package com.fyp115494258.restreserve.Common;

import android.location.Location;

import com.fyp115494258.restreserve.Model.Restaurant;
import com.fyp115494258.restreserve.Model.User;

import java.util.Map;

public class Common {

    public static final int REQUEST_CODE = 1000;
    public static User currentUser;

    public static Restaurant currentRestaurant;

    public static String restId;

    public static final String INTENT_RESTAURANT_ID="RestaurantId";

    public static void updateLocation(String restId, Location mLastLocation) {


    }
}
