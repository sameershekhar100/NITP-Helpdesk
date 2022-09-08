package com.example.sample;

public class Complain {
    String location,description,number;
    int status;
    String category;

    //constructor
    public Complain(String location, String description, String number, int status, String category) {
        this.location = location;
        this.description = description;
        this.number = number;
        this.status = status;
        this.category = category;
    }

    //getters and setters
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
