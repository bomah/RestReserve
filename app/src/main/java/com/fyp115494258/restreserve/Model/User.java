package com.fyp115494258.restreserve.Model;

public class User {

    private String name;
    private String password;
    private String phoneNumber;

    public User(){

    }

    public User(String Name, String Password,String PhoneNumber){
        setName(Name);
        setPassword(Password);
        setPhoneNumber(PhoneNumber);

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
