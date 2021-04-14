package com.fundmanagement.Model;

public class PriorGData {
    String id="",status="",date="",email="";

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    String collectionId;
    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public PriorGData(String id, String status, String date, String email) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.email = email;
    }
}
