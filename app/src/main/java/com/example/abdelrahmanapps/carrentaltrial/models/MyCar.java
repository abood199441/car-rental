package com.example.abdelrahmanapps.carrentaltrial.models;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MyCar implements Comparable<MyCar> {
    private String brandData, gasData,seatNumberData,
            statusData,imageURL,
            model,description,price,size, transmission,
            kiloMeters,carID,userPhoneNumber,userID,ownerName,address;
    private Double latitude,longitude;

    public MyCar() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public MyCar(String brandData, String gasData, String seatNumberData,
                 String statusData, String imageURL, String model, String description,
                 String price, String size, String transmission, String kiloMeters,
                 String carID, String userPhoneNumber, String userID, String ownerName,
                 Double latitude, Double longitude,String address) {
        this.brandData = brandData;
        this.gasData = gasData;
        this.seatNumberData = seatNumberData;
        this.statusData = statusData;
        this.imageURL = imageURL;
        this.model = model;
        this.description = description;
        this.price = price;
        this.size = size;
        this.transmission = transmission;
        this.kiloMeters = kiloMeters;
        this.carID = carID;
        this.userPhoneNumber = userPhoneNumber;
        this.userID = userID;
        this.ownerName = ownerName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }


    public MyCar(String brandData, String gasData,
                 String seatNumberData, String statusData,
                 String imageURL, String model,
                 String description, String price, String size,
                 String transmission, String kiloMeters,
                 String carID, Double latitude, Double longitude,String address) {
        this.brandData = brandData;
        this.gasData = gasData;
        this.seatNumberData = seatNumberData;
        this.statusData = statusData;
        this.imageURL = imageURL;
        this.model = model;
        this.description = description;
        this.price = price;
        this.size = size;
        this.transmission = transmission;
        this.kiloMeters = kiloMeters;
        this.carID = carID;
        this.latitude = latitude;
        this.longitude = longitude;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.userID = user.getUid();
        this.ownerName = user.getDisplayName();
        this.userPhoneNumber = user.getPhoneNumber();
        this.address = address;

    }

    public String getBrandData() {
        return brandData;
    }

    public String getGasData() {
        return gasData;
    }

    public String getSeatNumberData() {
        return seatNumberData;
    }

    public String getStatusData() {
        return statusData;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getKiloMeters() {
        return kiloMeters;
    }

    public String getCarID() {
        return carID;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "MyCar{" +
                "brandData='" + brandData + '\'' +
                ", gasData='" + gasData + '\'' +
                ", seatNumberData='" + seatNumberData + '\'' +
                ", statusData='" + statusData + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", model='" + model + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", size='" + size + '\'' +
                ", transmission='" + transmission + '\'' +
                ", kiloMeters='" + kiloMeters + '\'' +
                ", carID='" + carID + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userID='" + userID + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("BRAND",brandData);
        bundle.putString("GAS",gasData);
        bundle.putString("SEATS",seatNumberData);
        bundle.putString("STATUS",statusData);
        bundle.putString("IMAGE",imageURL);
        bundle.putString("MODEL",model);
        bundle.putString("DESCRIPTION",description);
        bundle.putString("PRICE",price);
        bundle.putString("SIZE",size);
        bundle.putString("TRANSMISSION",transmission);
        bundle.putString("COUNTER",kiloMeters);
        bundle.putString("CARID",carID);
        bundle.putString("PHONE",userPhoneNumber);
        bundle.putString("USERID",userID);
        bundle.putString("NAME",ownerName);
        bundle.putString("ADDRESS",address);
        bundle.putDouble("LATITUDE",latitude);
        bundle.putDouble("LONGITUDE",longitude);
        return bundle;
    }

    @Override
    public int compareTo(MyCar o) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitude - o.latitude);
        double lonDistance = Math.toRadians(longitude - o.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(o.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        return (int) distance;

    }
}
