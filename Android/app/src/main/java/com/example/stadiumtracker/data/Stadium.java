package com.example.stadiumtracker.data;

public class Stadium implements java.io.Serializable{
    private int stadiumID;
    private String name;
    private String city;
    private String country;
    private double gpsLat;
    private double gpsLong;
    private String imageURL;

    /* John Strauser
        Default constructor that is only used for setting a 'prompt' value in the RecordActivity stadium dropdown menu
     */
    public Stadium(){
        stadiumID = -1;
        name = "Stadium";
        city = "";
        country = "";
        /* John Strauser
            This latitude and longitude translates to the south pole (according to google).
            Used so that this stadium should never be returned by the GPS button in RecordActivity
         */
        gpsLat = 90.0000;
        gpsLong = 45.0000;
        imageURL = "";
    }

    /* John Strauser
        Generic constructor for the stadium class
     */
    public Stadium(int stadiumID, String name, String city, String country, double gpsLat, double gpsLong, String imageURL){
        this.stadiumID=stadiumID;
        this.name=name;
        this.city=city;
        this.country=country;
        this.gpsLat=gpsLat;
        this.gpsLong=gpsLong;
        this.imageURL=imageURL;
    }
    /* John Strauser
        Auto-generated getters and setters
     */
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /* John Strauser
        to string simply returns the name of the stadium
     */
    public String toString(){
        return name;
    }
}
