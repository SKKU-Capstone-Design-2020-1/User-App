package com.skku.userweb.util;

public class Store {
    private String storeImage;
    private String storeName;
    private String storeAddress;
    private String remainedSeat;
    public Store(String storeImage, String storeName, String storeAddress, String remainedSeat){
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.remainedSeat = remainedSeat;
    }
    public String getStoreImage(){
        return this.storeImage;
    }
    public String getStoreName(){
        return this.storeName;
    }
    public String getStoreAddress(){
        return this.storeAddress;
    }
    public String getRemainedSeat(){
        return this.remainedSeat;
    }
}
