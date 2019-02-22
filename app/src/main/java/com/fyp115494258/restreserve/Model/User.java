package com.fyp115494258.restreserve.Model;

public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;


    public User(){

    }

    public User(String Id,String FirstName,String LastName,String PhoneNumber,String Email,String Password){
        setId(Id);
        setFirstName(FirstName);
        setLastName(LastName);
        setPhoneNumber(PhoneNumber);
        setEmail(Email);
        setPassword(Password);


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
