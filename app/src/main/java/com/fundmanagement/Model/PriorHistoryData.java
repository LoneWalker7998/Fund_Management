package com.fundmanagement.Model;

import java.io.Serializable;

public class PriorHistoryData implements Serializable {
    String requestNo="";
    String priorStatus="";

    public PriorHistoryData(String requestNo, String priorStatus) {
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
