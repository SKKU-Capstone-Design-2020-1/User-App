package com.skku.userweb.util;

public class Store {
    private String storeImage;
    private String storeName;
    private String storeAddress;
    private String remainedSeat;
    private String storeid;
    private String ownerEmail;
    public Store(String storeImage, String storeName, String storeAddress, String remainedSeat, String storeid, String ownerEmail){
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.remainedSeat = remainedSeat;
        this.storeid=storeid;
        this.ownerEmail=ownerEmail;
    }
    public String getStoreImage(){
        return this.storeImage;
    }
    public String getOwnerEmail(){
        return this.ownerEmail;
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
    public String getStoreId(){
        return this.storeid;
    }
}
