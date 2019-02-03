package com.fyp115494258.restreserve.Model;

public class Rating {

    private String userPhone;
    private String restaurantId;
    private String rateValue;
    private String comment;

    public Rating(){

    }

    public Rating(String UserPhone, String RestaurantId, String RateValue, String Comment){

        this.setUserPhone(UserPhone);
        this.setRestaurantId(RestaurantId);
        this.setRateValue(RateValue);
        this.setComment(Comment);
    }


    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
