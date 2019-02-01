package com.fyp115494258.restreserve.Model;

public class Restaurant {

    private String name;
    private String location;
    private String description;
    private String phoneNumber;
    private String image;
    private String adminPhoneNumber;



    public Restaurant(){

    }

    public Restaurant(String Name,String Location,String Description,String PhoneNumber,String Image,String AdminPhoneNumber){

        setName(Name);
        setLocation(Location);
        setDescription(Description);
        setPhoneNumber(PhoneNumber);
        setImage(Image);
        setAdminPhoneNumber(AdminPhoneNumber);

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getAdminPhoneNumber() {
        return adminPhoneNumber;
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        this.adminPhoneNumber = adminPhoneNumber;
    }
}
