package bamin.com.bamindaijia.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/18.
 */
public class OrderIng implements Serializable{
    /**
     * driverId : 11
     * driverName : 张三
     * driverPhone : 15871105320
     * driverOrderSum : 0
     * driverComeTime : 1
     * driverImage : null
     * driverGetIdTime : 201.1-5-11
     * orderStatus : 已接单
     * orderTime : 2016-07-19 14:17:04.0
     * startLocation : 联谊大厦
     * endLocation : 中国邮政储蓄银行(禾山支行)
     * id : 238
     */

    private int driverId;
    private String driverName;
    private String driverPhone;
    private int driverOrderSum;
    private int driverComeTime;
    private String driverImage;
    private String driverGetIdTime;
    private String orderStatus;
    private String orderTime;
    private String startLocation;
    private String endLocation;
    private int id;

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public int getDriverOrderSum() {
        return driverOrderSum;
    }

    public void setDriverOrderSum(int driverOrderSum) {
        this.driverOrderSum = driverOrderSum;
    }

    public int getDriverComeTime() {
        return driverComeTime;
    }

    public void setDriverComeTime(int driverComeTime) {
        this.driverComeTime = driverComeTime;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverGetIdTime() {
        return driverGetIdTime;
    }

    public void setDriverGetIdTime(String driverGetIdTime) {
        this.driverGetIdTime = driverGetIdTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderIng{" +
                "driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", driverOrderSum=" + driverOrderSum +
                ", driverComeTime=" + driverComeTime +
                ", driverImage='" + driverImage + '\'' +
                ", driverGetIdTime='" + driverGetIdTime + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", id=" + id +
                '}';
    }
}
