package com.fundmanagement.Model;

import java.io.Serializable;

public class PriorHistoryData implements Serializable {
    String requestNo="";
    String priorStatus="";
    String id ="";
    String date;

    public PriorHistoryData(String requestNo, String priorStatus, String date) {
        this.requestNo = requestNo;
        this.priorStatus = priorStatus;
        this.date = date;
    }
    public String getDate() {
        return date;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestNo(){
        return requestNo;
    }

    public String getPriorStatus() {
        return priorStatus;
    }
}
