package net.bingyan.coverit.data.local.bean;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Author       zdlly
 * Date         2018.4.2
 * Time         23:41
 */
public class ParentListBean extends RealmObject {
    private String title;
    private Date date;
    private String text;
    private boolean isTop;
    private String picpath;
    private String belonging;


    public String getBelonging() {
        return belonging;
    }

    public void setBelonging(String belonging) {
        this.belonging = belonging;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }
}
