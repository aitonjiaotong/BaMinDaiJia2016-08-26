package bamin.com.bamindaijia.model;

/**
 * Created by zjb on 2016/7/8.
 */
public class YuGuPrice {
    /**
     * status : 0
     * mes :
     * datas : {"unit":null,"money":18,"baseFee":18,"unitFee":20,"unitMileage":5,"baseMileage":10}
     */

    private int status;
    private String mes;
    /**
     * unit : null
     * money : 18.0
     * baseFee : 18.0
     * unitFee : 20.0
     * unitMileage : 5
     * baseMileage : 10
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
        private double unit;
        private double money;
        private double baseFee;
        private double unitFee;
        private double unitMileage;
        private double baseMileage;
        private double mileage;

        public double getMileage() {
            return mileage;
        }

        public void setMileage(double mileage) {
            this.mileage = mileage;
        }

        public double getUnit() {
            return unit;
        }

        public void setUnit(double unit) {
            this.unit = unit;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getBaseFee() {
            return baseFee;
        }

        public void setBaseFee(double baseFee) {
            this.baseFee = baseFee;
        }

        public double getUnitFee() {
            return unitFee;
        }

        public void setUnitFee(double unitFee) {
            this.unitFee = unitFee;
        }

        public double getUnitMileage() {
            return unitMileage;
        }

        public void setUnitMileage(double unitMileage) {
            this.unitMileage = unitMileage;
        }

        public double getBaseMileage() {
            return baseMileage;
        }

        public void setBaseMileage(double baseMileage) {
            this.baseMileage = baseMileage;
        }
    }
}
