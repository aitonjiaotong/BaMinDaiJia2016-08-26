package bamin.com.bamindaijia.model;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DriverInfo {
    /**
     * status : 0
     * mes : 请求成功
     * datas : {"id":10,"key":null,"name":"cao","phone":"18250799709","cardId":null,"drivingId":null,"drivingImg":null,"driverStatus":"空闲","auditingStatus":"合格通过","giveUpNum":0,"orderSum":0,"recordId":null,"accountStatus":"正常","serviceCity":"厦门市","serviceScore":null,"password":null,"urgentPhone":null,"deviceid":"1234","image":null,"reserve1":"0","reserve2":null,"reserve3":null,"reserve4":null,"reserve5":null,"cardImg":null}
     */

    private int status;
    private String mes;
    /**
     * id : 10
     * key : null
     * name : cao
     * phone : 18250799709
     * cardId : null
     * drivingId : null
     * drivingImg : null
     * driverStatus : 空闲
     * auditingStatus : 合格通过
     * giveUpNum : 0
     * orderSum : 0
     * recordId : null
     * accountStatus : 正常
     * serviceCity : 厦门市
     * serviceScore : null
     * password : null
     * urgentPhone : null
     * deviceid : 1234
     * image : null
     * reserve1 : 0
     * reserve2 : null
     * reserve3 : null
     * reserve4 : null
     * reserve5 : null
     * cardImg : null
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

    public static class DatasEntity {
        private int id;
        private String key;
        private String name;
        private String phone;
        private String cardId;
        private String drivingId;
        private String drivingImg;
        private String driverStatus;
        private String auditingStatus;
        private int giveUpNum;
        private int orderSum;
        private String recordId;
        private String accountStatus;
        private String serviceCity;
        private String serviceScore;
        private String password;
        private String urgentPhone;
        private String deviceid;
        private String image;
        private String reserve1;
        private String reserve2;
        private String reserve3;
        private String reserve4;
        private String reserve5;
        private String cardImg;

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

        public int getGiveUpNum() {
            return giveUpNum;
        }

        public void setGiveUpNum(int giveUpNum) {
            this.giveUpNum = giveUpNum;
        }

        public int getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(int orderSum) {
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

        public String getServiceScore() {
            return serviceScore;
        }

        public void setServiceScore(String serviceScore) {
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

        public String getCardImg() {
            return cardImg;
        }

        public void setCardImg(String cardImg) {
            this.cardImg = cardImg;
        }
    }
}
