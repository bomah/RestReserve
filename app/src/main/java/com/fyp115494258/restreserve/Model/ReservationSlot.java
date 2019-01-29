package com.fyp115494258.restreserve.Model;

public class ReservationSlot {



    private String date;
    private String time;
    private String restaurantId;

    //
    private String dateRestaurantId;

    private int numberOfPeople;


    public ReservationSlot(){

    }

    public ReservationSlot(String Date,String Time,String RestaurantId,String DateRestaurantId,int NumberOfPeople){

        setDate(Date);
        setTime(Time);
        setRestaurantId(RestaurantId);
        setDateRestaurantId(DateRestaurantId);
        setNumberOfPeople(NumberOfPeople);

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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateRestaurantId() {
        return dateRestaurantId;
    }

    public void setDateRestaurantId(String dateRestaurantId) {
        this.dateRestaurantId = dateRestaurantId;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
