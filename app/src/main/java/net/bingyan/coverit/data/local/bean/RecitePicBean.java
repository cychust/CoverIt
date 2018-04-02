package net.bingyan.coverit.data.local.bean;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Author       zdlly
 * Date         2018.3.28
 * Time         23:39
 */

public class RecitePicBean extends RealmObject {
    private String picTitle;
    private Date picDate;
    private String picPath;
    private boolean isTop;
    private RealmList<PicConfigBean> picConfigList;

    public String getPicTitle() {
        return picTitle;
    }

    public void setPicTitle(String picTitle) {
        this.picTitle = picTitle;
    }

    public Date getPicDate() {
        return picDate;
    }

    public void setPicDate(Date picDate) {
        this.picDate = picDate;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public RealmList<PicConfigBean> getPicConfigList() {
        return picConfigList;
    }

    public void setPicConfigList(RealmList<PicConfigBean> picConfigList) {
        this.picConfigList = picConfigList;
    }
}
