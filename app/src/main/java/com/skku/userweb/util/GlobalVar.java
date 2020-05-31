package com.skku.userweb.util;

import android.app.Application;

public class GlobalVar extends Application {
    private String imgUrl;
    private String userId;
    private String storeId;
    private String storeName;
    private String totalSeat;
    public String getImgUrl(){
        return imgUrl;
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

}

