package com.fyp115494258.restreserve.Model;

public class Reservation {

    private String restaurantName;
    private String date;
    private String time;
    private int numberOfPeople;

    public Reservation(){

    }

    public Reservation(String RestaurantName,String Date, String Time, int NumberOfPeople){

        setRestaurantName(RestaurantName);
        setDate(Date);
        setTime(Time);
        setNumberOfPeople(NumberOfPeople);


    }


    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
