package com.fundmanagement.Model;

public class FundRequestData {
    String ARR_no="";
    String name="";
    String status="";
    String collectionId;

    public FundRequestData(String ARR_no, String name, String status) {
        this.ARR_no = ARR_no;
        this.name = name;
        this.status = status;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getARR_no() {
        return ARR_no;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getCollectionId() {
        return collectionId;
    }
}
