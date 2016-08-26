package com.aiton.bamin.bamindaijiadrier.model;

/**
 * Created by Administrator on 2016/8/15.
 */
public class PaySuccess {
    private String payStatus;
    private double cost;
    private double fee;
    private String orderId;

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
}
