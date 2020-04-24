package com.skku.userweb.util;

public class Store {
    private int storeImage;
    private String storeName;
    private String storeAddress;
    private String remainedSeat;
    public Store(int storeImage, String storeName, String storeAddress, String remainedSeat){
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.remainedSeat = remainedSeat;
    }
    public int getStoreImage(){
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
