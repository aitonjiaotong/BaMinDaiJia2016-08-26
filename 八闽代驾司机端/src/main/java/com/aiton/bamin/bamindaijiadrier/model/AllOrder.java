package com.aiton.bamin.bamindaijiadrier.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class AllOrder {

    /**
     * status : 0
     * mes : 查询订单成功
     * datas :
     */

    private int status;
    private String mes;
    /**
     * id : 398
     * orderDate : 2016-08-16 11:25:38
     * orderTime : 1471317938000
     * customerId : 10
     * customerName : 张先生
     * customerPhone : 13599749127
     * driverId : 10
     * driverName : cao
     * driverPhone : 18250799709
     * startLocation : 联谊大厦(禾山一路)
     * startCoordinate : 118.13549,24.506504
     * endLocation : 嘉禾公寓(园山南路)
     * endCoordinate : 118.134449,24.506678
     * orderStatus : 取消
     * cancelCause : 有人来接，。
     * orderStart : 1471317946000
     * orderComplete : 1471317952000
     * mileage : 0.0
     * carNumber : null
     * money : 0.01
     * orderCity : 厦门市
     * payMode : null
     * orderId : 1471317938368
     * fee : 0.0
     * key : 9dccf5e6c80cebf62f8237a1d2fe9de0
     * driverCoordinate : 118.13543,24.50652
     * driverComeTime : 1
     * cost : 0.01
     * changeMileage : 0.0
     * redPaperId : null
     * redPaperMoney : null
     * finalPrice : null
     */

    private List<DatasBean> datas;

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

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
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
        private long orderStart;
        private long orderComplete;
        private double mileage;
        private Object carNumber;
        private double money;
        private String orderCity;
        private Object payMode;
        private String orderId;
        private double fee;
        private String key;
        private String driverCoordinate;
        private int driverComeTime;
        private double cost;
        private double changeMileage;
        private Object redPaperId;
        private Object redPaperMoney;
        private Object finalPrice;

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

        public long getOrderStart() {
            return orderStart;
        }

        public void setOrderStart(long orderStart) {
            this.orderStart = orderStart;
        }

        public long getOrderComplete() {
            return orderComplete;
        }

        public void setOrderComplete(long orderComplete) {
            this.orderComplete = orderComplete;
        }

        public double getMileage() {
            return mileage;
        }

        public void setMileage(double mileage) {
            this.mileage = mileage;
        }

        public Object getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(Object carNumber) {
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

        public Object getPayMode() {
            return payMode;
        }

        public void setPayMode(Object payMode) {
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

        public String getDriverCoordinate() {
            return driverCoordinate;
        }

        public void setDriverCoordinate(String driverCoordinate) {
            this.driverCoordinate = driverCoordinate;
        }

        public int getDriverComeTime() {
            return driverComeTime;
        }

        public void setDriverComeTime(int driverComeTime) {
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

        public Object getRedPaperId() {
            return redPaperId;
        }

        public void setRedPaperId(Object redPaperId) {
            this.redPaperId = redPaperId;
        }

        public Object getRedPaperMoney() {
            return redPaperMoney;
        }

        public void setRedPaperMoney(Object redPaperMoney) {
            this.redPaperMoney = redPaperMoney;
        }

        public Object getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(Object finalPrice) {
            this.finalPrice = finalPrice;
        }
    }
}
