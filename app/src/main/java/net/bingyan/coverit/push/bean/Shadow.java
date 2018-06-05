package net.bingyan.coverit.push.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shadow {

    @SerializedName("fromNumber")
    @Expose
    private Integer fromNumber;
    @SerializedName("toNumber")
    @Expose
    private Integer toNumber;

    public Integer getFromNumber() {
        return fromNumber;
    }

    public void setFrom(Integer fromNumber) {
        this.fromNumber = fromNumber;
    }

    public Integer getToNumber() {
        return toNumber;
    }

    public void setToNumber(Integer toNumber) {
        this.toNumber = toNumber;
    }

}