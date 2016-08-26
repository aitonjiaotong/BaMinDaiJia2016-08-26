package com.aiton.bamin.bamindaijiadrier.model;

import java.io.Serializable;

/**
 * Created by zjb on 2016/7/15.
 */
public class Order implements Serializable{
    /**
     * status : 0
     * mes : 抢单成功
     * datas : {"id":194,"orderDate":"2016-07-15 13:49:04","orderTime":1468561744000,"customerId":9,"customerName":"张先生","customerPhone":"15871105320","driverId":12,"driverName":"张司机","driverPhone":"13599749127","startLocation":"联谊大厦","startCoordinate":"118.135463,24.506494","endLocation":"安兜","endCoordinate":"118.134395,24.505562","orderStatus":"已接单","cancelCause":null,"orderStart":null,"orderComplete":null,"mileage":0,"carNumber":null,"money":18,"orderCity":"厦门市","payMode":null,"orderId":"1468561744974","fee":0,"key":"4116377535605432836","reserve3":null,"reserve4":null,"reserve5":null}
     */

    private int status;
    private String mes;
    /**
     * id : 194
     * orderDate : 2016-07-15 13:49:04
     * orderTime : 1468561744000
     * customerId : 9
     * customerName : 张先生
     * customerPhone : 15871105320
     * driverId : 12
     * driverName : 张司机
     * driverPhone : 13599749127
     * startLocation : 联谊大厦
     * startCoordinate : 118.135463,24.506494
     * endLocation : 安兜
     * endCoordinate : 118.134395,24.505562
     * orderStatus : 已接单
     * cancelCause : null
     * orderStart : null
     * orderComplete : null
     * mileage : 0.0
     * carNumber : null
     * money : 18.0
     * orderCity : 厦门市
     * payMode : null
     * orderId : 1468561744974 流水号
     * fee : 0.0
     * key : 4116377535605432836
     * reserve3 : null
     * reserve4 : null
     * cost : null
     */

    private DatasEntity datas;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public DatasEntity getDatas() {
        return datas;
    }

    public void setDatas(DatasEntity datas) {
        this.datas = datas;
    }

    public static class DatasEntity implements Serializable{
        private int id;
        private String orderDate;
        private long orderTime;
        private int customerId;
        private String customerName;
        private String customerPhone;
        private int driverId;
        private String driverName;
        private String driverPhone;
        private String startLocation;
        private String startCoordinate;
        private String endLocation;
        private String endCoordinate;
        private String orderStatus;
        private String cancelCause;
        private String orderStart;
        private String orderComplete;
        private double mileage;
        private String carNumber;
        private double money;
        private String orderCity;
        private String payMode;
        private String orderId;
        private double fee;
        private String key;
        private String driverCoordinate;
        private String driverComeTime;
        private double cost;
        private double changeMileage;

        public String getDriverCoordinate() {
            return driverCoordinate;
        }

        public void setDriverCoordinate(String driverCoordinate) {
            this.driverCoordinate = driverCoordinate;
        }

        public String getDriverComeTime() {
            return driverComeTime;
        }

        public void setDriverComeTime(String driverComeTime) {
            this.driverComeTime = driverComeTime;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getChangeMileage() {
            return changeMileage;
        }

        public void setChangeMileage(double changeMileage) {
            this.changeMileage = changeMileage;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

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

        public String getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(String startLocation) {
            this.startLocation = startLocation;
        }

        public String getStartCoordinate() {
            return startCoordinate;
        }

        public void setStartCoordinate(String startCoordinate) {
            this.startCoordinate = startCoordinate;
        }

        public String getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(String endLocation) {
            this.endLocation = endLocation;
        }

        public String getEndCoordinate() {
            return endCoordinate;
        }

        public void setEndCoordinate(String endCoordinate) {
            this.endCoordinate = endCoordinate;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCancelCause() {
            return cancelCause;
        }

        public void setCancelCause(String cancelCause) {
            this.cancelCause = cancelCause;
        }

        public String getOrderStart() {
            return orderStart;
        }

        public void setOrderStart(String orderStart) {
            this.orderStart = orderStart;
        }

        public String getOrderComplete() {
            return orderComplete;
        }

        public void setOrderComplete(String orderComplete) {
            this.orderComplete = orderComplete;
        }

        public double getMileage() {
            return mileage;
        }

        public void setMileage(double mileage) {
            this.mileage = mileage;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getOrderCity() {
            return orderCity;
        }

        public void setOrderCity(String orderCity) {
            this.orderCity = orderCity;
        }

        public String getPayMode() {
            return payMode;
        }

        public void setPayMode(String payMode) {
            this.payMode = payMode;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }


    }
}
