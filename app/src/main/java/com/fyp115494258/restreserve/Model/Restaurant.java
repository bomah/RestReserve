package com.fyp115494258.restreserve.Model;

public class Restaurant {

    private String name;
    private String address;
    private double lat;
    private double lng;
    private String description;
    private String phoneNumber;
    private String image;
    private String adminEmail;



    public Restaurant(){

    }

    public Restaurant(String Name,String Address,double Lat,double Lng,String Description,String PhoneNumber,String Image,String AdminEmail){

        setName(Name);
        setAddress(Address);
        setLat(Lat);
        setLng(Lng);

        setDescription(Description);
        setPhoneNumber(PhoneNumber);
        setImage(Image);
        setAdminEmail(AdminEmail);

    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
