package com.fyp115494258.restreserve.Model;

import org.w3c.dom.Comment;

public class MainCourse {

    private String foodName;
    private String price;
    private String restaurantId;

    public MainCourse(){

    }

    public MainCourse(String FoodName, String Price, String RestaurantId){

        this.setFoodName(FoodName);
        this.setPrice(Price);
        this.setRestaurantId(RestaurantId);


    }



    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
