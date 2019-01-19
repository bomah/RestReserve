package com.fyp115494258.restreserve.Model;

public class ReservationSlot {


    private String time;
    private String restaurantId;

    public ReservationSlot(){

    }

    public ReservationSlot(String Time,String RestaurantId){

        setTime(Time);
        setRestaurantId(RestaurantId);

    }





    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
