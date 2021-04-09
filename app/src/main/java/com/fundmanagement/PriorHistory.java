package com.fundmanagement;

import java.io.Serializable;

public class PriorHistory implements Serializable {
    String requestNo="";
    String priorStatus="";

    public PriorHistory(String requestNo, String priorStatus) {
        this.requestNo = requestNo;
        this.priorStatus = priorStatus;
    }

    public String getRequestNo(){
        return requestNo;
    }

    public String getPriorStatus() {
        return priorStatus;
    }
}
