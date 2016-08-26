package bamin.com.bamindaijia.model;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class MyTip implements Serializable{

    private String a;
    private MyLatLonPoint b;
    private String c;
    private String d;
    private String e;
    private String f;

    public MyTip() {
    }

    public String getPoiID() {
        return this.a;
    }

    public void setID(String var1) {
        this.a = var1;
    }

    public MyLatLonPoint getPoint() {
        return this.b;
    }

    public void setPostion(MyLatLonPoint var1) {
        this.b = var1;
    }

    public String getName() {
        return this.c;
    }

    public void setName(String var1) {
        this.c = var1;
    }

    public String getDistrict() {
        return this.d;
    }

    public void setDistrict(String var1) {
        this.d = var1;
    }

    public String getAdcode() {
        return this.e;
    }

    public void setAdcode(String var1) {
        this.e = var1;
    }

    public String getAddress() {
        return this.f;
    }

    public void setAddress(String var1) {
        this.f = var1;
    }

    public String toString() {
        return "name:" + this.c + " district:" + this.d + " adcode:" + this.e;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeString(this.c);
        var1.writeString(this.e);
        var1.writeString(this.d);
        var1.writeString(this.a);
        var1.writeValue(this.b);
        var1.writeString(this.f);
    }

    private MyTip(Parcel var1) {
        this.c = var1.readString();
        this.e = var1.readString();
        this.d = var1.readString();
        this.a = var1.readString();
        this.b = (MyLatLonPoint)var1.readValue(MyLatLonPoint.class.getClassLoader());
        this.f = var1.readString();
    }
}
