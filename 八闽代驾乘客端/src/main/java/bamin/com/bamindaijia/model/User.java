package bamin.com.bamindaijia.model;

import java.io.Serializable;

/**
 * Created by zjb on 2016/6/24.
 */
public class User implements Serializable{
    public User(int status, String mes, DatasEntity datas) {
        this.status = status;
        this.mes = mes;
        this.datas = datas;
    }

    /**
     * status : 0
     * mes : 登入成功
     * datas : {"id":9,"name":null,"phone":"15871105320","cancelNum":null,"password":null,"image":null,"deviceid":"76767657"}
     */

    private int status;
    private String mes;
    /**
     * id : 9
     * name : null
     * phone : 15871105320
     * cancelNum : null
     * password : null
     * image : null
     * deviceid : 76767657
     * reserve4 :
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
        public DatasEntity(int id, String key, String name, String phone, String cancelNum, String password, String image, String deviceid, String reserve1, String reserve2, String reserve3, String reserve4, String reserve5) {
            this.id = id;
            this.key = key;
            this.name = name;
            this.phone = phone;
            this.cancelNum = cancelNum;
            this.password = password;
            this.image = image;
            this.deviceid = deviceid;
            this.reserve1 = reserve1;
            this.reserve2 = reserve2;
            this.reserve3 = reserve3;
            this.reserve4 = reserve4;
            this.reserve5 = reserve5;
        }

        private int id;
        private String key;
        private String name;
        private String phone;
        private String cancelNum;
        private String password;
        private String image;
        private String deviceid;
        private String reserve1;         //假删除
        private String reserve2;         //司机是否离线
        private String reserve3;         //性别
        private String reserve4;         //备用字段4
        private String reserve5;         //备用字段5


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
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

        public String getCancelNum() {
            return cancelNum;
        }

        public void setCancelNum(String cancelNum) {
            this.cancelNum = cancelNum;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }
    }
}
