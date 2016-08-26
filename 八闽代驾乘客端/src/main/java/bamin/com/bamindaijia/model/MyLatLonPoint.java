package bamin.com.bamindaijia.model;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class MyLatLonPoint implements Serializable {
    private double a;
    private double b;

    public MyLatLonPoint(double var1, double var3) {
        this.a = var1;
        this.b = var3;
    }

    public double getLongitude() {
        return this.b;
    }

    public void setLongitude(double var1) {
        this.b = var1;
    }

    public double getLatitude() {
        return this.a;
    }

    public void setLatitude(double var1) {
        this.a = var1;
    }

    public MyLatLonPoint copy() {
        return new MyLatLonPoint(this.a, this.b);
    }

    public int hashCode() {
        boolean var1 = true;
        byte var2 = 1;
        long var3 = Double.doubleToLongBits(this.a);
        int var5 = 31 * var2 + (int) (var3 ^ var3 >>> 32);
        var3 = Double.doubleToLongBits(this.b);
        var5 = 31 * var5 + (int) (var3 ^ var3 >>> 32);
        return var5;
    }


    public String toString() {
        return "" + this.a + "," + this.b;
    }

    protected MyLatLonPoint(Parcel var1) {
        this.a = var1.readDouble();
        this.b = var1.readDouble();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeDouble(this.a);
        var1.writeDouble(this.b);
    }
}

