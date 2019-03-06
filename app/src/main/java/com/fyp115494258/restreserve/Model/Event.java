package com.fyp115494258.restreserve.Model;

public class Event {

    private String name;
    private String description;
    private String image;


    public Event(){


    }


    public Event(String Name,String Description,String Image){


        this.setName(Name);
        this.setDescription(Description);
        this.setImage(Image);


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
