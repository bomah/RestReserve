package com.fyp115494258.restreserve.Model;

public class User {

    private String name;
    private String password;

    public User(){

    }

    public User(String Name, String Password){
        setName(Name);
        setPassword(Password);

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
}
