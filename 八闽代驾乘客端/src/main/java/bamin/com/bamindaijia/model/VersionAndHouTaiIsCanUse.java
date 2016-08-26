package bamin.com.bamindaijia.model;

/**
 * Created by zjb on 2016/6/27.
 */
public class VersionAndHouTaiIsCanUse {

    /**
     * status : 0
     * mes : 请求成功
     * datas : {"id":1,"ableVersion":0,"message":"","live":true}
     */

    private int status;
    private String mes;
    /**
     * id : 1
     * ableVersion : 0
     * message :
     * live : true
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
        private int ableVersion;
        private String message;
        private boolean live;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAbleVersion() {
            return ableVersion;
        }

        public void setAbleVersion(int ableVersion) {
            this.ableVersion = ableVersion;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isLive() {
            return live;
        }

        public void setLive(boolean live) {
            this.live = live;
        }
    }
}
