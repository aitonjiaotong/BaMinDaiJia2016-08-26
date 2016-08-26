package bamin.com.bamindaijia.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/25.
 */
public class Cost implements Serializable{
    private double changeMileage;
    private String meassage;
    private double cost;
    private double fee;
    private String orderId;

    public double getChangeMileage() {
        return changeMileage;
    }

    public void setChangeMileage(double changeMileage) {
        this.changeMileage = changeMileage;
    }

    public String getMeassage() {
        return meassage;
    }

    public void setMeassage(String meassage) {
        this.meassage = meassage;
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

    @Override
    public String toString() {
        return "Cost{" +
                "changeMileage=" + changeMileage +
                ", meassage='" + meassage + '\'' +
                ", cost=" + cost +
                ", fee=" + fee +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
