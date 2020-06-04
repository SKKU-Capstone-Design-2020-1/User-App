package com.skku.userweb.util;

import android.app.Application;

import com.google.firebase.firestore.DocumentReference;

public class GlobalVar extends Application {
    private String imgUrl;
    private String userId;
    private String storeId;
    private String storeName;
    private String totalSeat;
    private String map_id;
    private String seat_id;
    private String selected_seat;
    private String seatGroupId;
    private String beaconId;
    private String ownerEmail;
    private int gotime;
    private int breaktime;
    public String getImgUrl(){
        return imgUrl;
    }
    public void setOwnerEmail(String ownerEmail){
        this.ownerEmail = ownerEmail;
    }
    public String getOwnerEmail(){
        return ownerEmail;
    }
    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getStoreName(){
        return storeName;
    }
    public void setStoreName(String storeName){
        this.storeName = storeName;
    }
    public String getStoreId(){
        return storeId;
    }
    public void setStoreId(String storeId){
        this.storeId = storeId;
    }
    public String getTotalSeat(){
        return totalSeat;
    }
    public void setTotalSeat(String totalSeat){
        this.totalSeat = totalSeat;
    }

    public String getMap_id(){
        return map_id;
    }
    public void setMap_id(String map_id){
        this.map_id = map_id;
    }
    public String getSeat_id(){
        return seat_id;
    }
    public void setSeat_id(String seat_id){
        this.seat_id = seat_id;
    }
    public String getSelected_seat(){
        return selected_seat;
    }
    public void setSelected_seat(String selected_seat){
        this.selected_seat = selected_seat;
    }
    public String getSeatGroupId(){
        return seatGroupId;
    }
    public void setSeatGroupId(String seatGroupId){
        this.seatGroupId = seatGroupId;
    }
    public String getBeaconId(){
        return beaconId;
    }
    public void setBeaconId(String beaconId){
        this.beaconId = beaconId;
    }
    public int getGotime(){
        return gotime;
    }
    public void setGotime(int gotime){
        this.gotime = gotime;
    }
    public int getBreaktime(){
        return breaktime;
    }
    public void setBreaktime(int breaktime){
        this.breaktime = breaktime;
    }

}

