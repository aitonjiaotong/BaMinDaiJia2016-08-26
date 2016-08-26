package com.aiton.bamin.bamindaijiadrier.model;

/**
 * Created by Administrator on 2016/7/22.
 */
public class OrderStatue {

    /**
     * status : 0
     * mes : 服务开始
     * datas : null
     */

    private int status;
    private String mes;
    private Object datas;

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

    public Object getDatas() {
        return datas;
    }

    public void setDatas(Object datas) {
        this.datas = datas;
    }
}
