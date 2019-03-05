package com.fyp115494258.restreserve.Model;

public class Reservation {

    private String restaurantId;
    private String restaurantName;
    private String personName;
    private String personPhoneNumber;
    private String adminEmail;
    private String date;
    private String time;
    private String dateTime;
    private int numberOfPeople;

    public Reservation(){

    }

    public Reservation(String RestaurantId,String RestaurantName,String PersonName,String PersonPhoneNumber,String AdminEmail,String Date, String Time, String DateTime, int NumberOfPeople){

        setRestaurantId(RestaurantId);
        setRestaurantName(RestaurantName);
        setPersonName(PersonName);
        setPersonPhoneNumber(PersonPhoneNumber);
        setAdminEmail(AdminEmail);
        setDate(Date);
        setTime(Time);
        setDateTime(DateTime);
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

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonPhoneNumber() {
        return personPhoneNumber;
    }

    public void setPersonPhoneNumber(String personPhoneNumber) {
        this.personPhoneNumber = personPhoneNumber;
    }



    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
