package com.example.sample.Class;
import java.io.Serializable
        ;
@SuppressWarnings("serial")
public class Complain implements Serializable {

     String category;
     String description;
     String location;
     String name;
     long number;
     long status;
     String timeStamp;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }



    //constructor
    public Complain(String name,String location, String description, long number, long status, String category,String timeStamp) {
        this.location = location;
        this.name=name;
        this.description = description;
        this.number = number;
        this.status = status;
        this.category = category;
        this.timeStamp=timeStamp;
    }
    public  Complain(){

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

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
