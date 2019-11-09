package com.example.stadiumtracker.data;

public class Stadium implements java.io.Serializable{
    private int stadiumID;
    private String name;
    private String city;
    private String country;
    private double gpsLat;
    private double gpsLong;

    //This constructor should only really be used for the first entry in a dropdown
    public Stadium(){
        stadiumID = -1;
        name = "Stadium";
        city = "";
        country = "";
        //This latitude and longitude is literally the south pole. If they are closer to here than any stadium in our database they probably don't fit our use cases
        gpsLat = 90.0000;
        gpsLong = 45.0000;
    }

    public Stadium(int stadiumID, String name, String city, String country){
        this.stadiumID=stadiumID;
        this.name=name;
        this.city=city;
        this.country=country;
        this.gpsLat=0;
        this.gpsLong=0;
    }

    public Stadium(int stadiumID, String name, String city, String country, double gpsLat, double gpsLong){
        this.stadiumID=stadiumID;
        this.name=name;
        this.city=city;
        this.country=country;
        this.gpsLat=gpsLat;
        this.gpsLong=gpsLong;
    }

    public int getStadiumID() {
        return stadiumID;
    }

    public void setStadiumID(int stadiumID) {
        this.stadiumID = stadiumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(double gpsLat) {
        this.gpsLat = gpsLat;
    }

    public double getGpsLong() {
        return gpsLong;
    }

    public void setGpsLong(double gpsLong) {
        this.gpsLong = gpsLong;
    }

    public String toString(){
        return name;
    }
}
