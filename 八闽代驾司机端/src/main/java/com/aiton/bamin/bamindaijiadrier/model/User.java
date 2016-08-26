package com.aiton.bamin.bamindaijiadrier.model;

import java.io.Serializable;

/**
 * Created by zjb on 2016/6/24.
 */
public class User implements Serializable{
    /**
     * status : 0
     * mes : 请求成功
     * datas : {"id":"10","key":"","name":"cao","phone":"18250799709","cardId":"","drivingId":"","drivingImg":"","driverStatus":"","auditingStatus":"","giveUpNum":"","orderSum":"","recordId":"","accountStatus":"","serviceCity":"","serviceScore":"","password":"","urgentPhone":"","deviceid":"123456","cardImg":""}
     */

    private int status;
    private String mes;
    /**
     * id : 10
     * key :
     * name : cao
     * phone : 18250799709
     * cardId :
     * drivingId :
     * drivingImg :
     * driverStatus :
     * auditingStatus :
     * giveUpNum :
     * orderSum :
     * recordId :
     * accountStatus :
     * serviceCity :
     * serviceScore :
     * password :
     * urgentPhone :
     * deviceid : 123456
     * cardImg :
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
        private String key;              //百度云推送channal ID
        private String name;             //司机名
        private String phone;            //司机电话
        private String cardId;            //身份证号
        private String drivingId;        //驾驶证号
        private String drivingImg;       //上传驾驶证图片
        private String cardImg;          //身份证图片
        private String driverStatus;    //司机状态
        private String  auditingStatus; //审核状态
        private Integer giveUpNum;        //弃单数量
        private Integer orderSum;         //接单数量
        private String recordId;         //司机档案ID
        private String accountStatus;    //账号状态
        private String serviceCity;      //服务城市
        private Integer serviceScore;    //服务评分
        private String password;         //密码
        private String urgentPhone;       //紧急联系人
        private String deviceid;         //手机设备的ID
        private String image;           //司机头像
        private String reserve1;         //备用字段1
        private String reserve2;         //备用字段2
        private String reserve3;         //备用字段3
        private String reserve4;         //备用字段4
        private String reserve5;         //备用字段5

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getReserve1() {
            return reserve1;
        }

        public void setReserve1(String reserve1) {
            this.reserve1 = reserve1;
        }

        public String getReserve2() {
            return reserve2;
        }

        public void setReserve2(String reserve2) {
            this.reserve2 = reserve2;
        }

        public String getReserve3() {
            return reserve3;
        }

        public void setReserve3(String reserve3) {
            this.reserve3 = reserve3;
        }

        public String getReserve4() {
            return reserve4;
        }

        public void setReserve4(String reserve4) {
            this.reserve4 = reserve4;
        }

        public String getReserve5() {
            return reserve5;
        }

        public void setReserve5(String reserve5) {
            this.reserve5 = reserve5;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getDrivingId() {
            return drivingId;
        }

        public void setDrivingId(String drivingId) {
            this.drivingId = drivingId;
        }

        public String getDrivingImg() {
            return drivingImg;
        }

        public void setDrivingImg(String drivingImg) {
            this.drivingImg = drivingImg;
        }

        public String getDriverStatus() {
            return driverStatus;
        }

        public void setDriverStatus(String driverStatus) {
            this.driverStatus = driverStatus;
        }

        public String getAuditingStatus() {
            return auditingStatus;
        }

        public void setAuditingStatus(String auditingStatus) {
            this.auditingStatus = auditingStatus;
        }

        public Integer getGiveUpNum() {
            return giveUpNum;
        }

        public void setGiveUpNum(Integer giveUpNum) {
            this.giveUpNum = giveUpNum;
        }

        public Integer getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(Integer orderSum) {
            this.orderSum = orderSum;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public String getServiceCity() {
            return serviceCity;
        }

        public void setServiceCity(String serviceCity) {
            this.serviceCity = serviceCity;
        }

        public Integer getServiceScore() {
            return serviceScore;
        }

        public void setServiceScore(Integer serviceScore) {
            this.serviceScore = serviceScore;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUrgentPhone() {
            return urgentPhone;
        }

        public void setUrgentPhone(String urgentPhone) {
            this.urgentPhone = urgentPhone;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getCardImg() {
            return cardImg;
        }

        public void setCardImg(String cardImg) {
            this.cardImg = cardImg;
        }
    }
}
