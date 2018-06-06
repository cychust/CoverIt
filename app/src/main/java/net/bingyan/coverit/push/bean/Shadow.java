package net.bingyan.coverit.push.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shadow {

    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setToNumber(Integer to) {
        this.to = to;
    }

}