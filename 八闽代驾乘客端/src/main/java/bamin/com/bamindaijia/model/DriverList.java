package bamin.com.bamindaijia.model;

import java.util.List;

/**
 * Created by zjb on 2016/7/7.
 */
public class DriverList {
    /**
     * status : 0
     * mes : null
     * datas : {"driverLocation":"118.13547,24.50648","driverTime":1,"driverDistance":1,"drivers":["118.13547,24.50648"]}
     */

    private int status;
    private String mes;
    /**
     * driverLocation : 118.13547,24.50648
     * driverTime : 1
     * driverDistance : 1
     * drivers : ["118.13547,24.50648"]
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
        private String driverLocation;
        private int driverTime;
        private int driverDistance;
        private List<String> drivers;

        public String getDriverLocation() {
            return driverLocation;
        }

        public void setDriverLocation(String driverLocation) {
            this.driverLocation = driverLocation;
        }

        public int getDriverTime() {
            return driverTime;
        }

        public void setDriverTime(int driverTime) {
            this.driverTime = driverTime;
        }

        public int getDriverDistance() {
            return driverDistance;
        }

        public void setDriverDistance(int driverDistance) {
            this.driverDistance = driverDistance;
        }

        public List<String> getDrivers() {
            return drivers;
        }

        public void setDrivers(List<String> drivers) {
            this.drivers = drivers;
        }
    }
}
