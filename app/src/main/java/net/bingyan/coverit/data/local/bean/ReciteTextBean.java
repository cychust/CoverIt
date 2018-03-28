package net.bingyan.coverit.data.local.bean;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Author       zdlly
 * Date         2018.3.28
 * Time         22:56
 */

public class ReciteTextBean extends RealmObject {
    private String textTitle;
    private Date textDate;
    private String textPath;
    private boolean isTop;
    private RealmList<TextConfigBean> textConfigList;

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public Date getTextDate() {
        return textDate;
    }

    public void setTextDate(Date textDate) {
        this.textDate = textDate;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setTextPath(String textPath) {
        this.textPath = textPath;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public RealmList<TextConfigBean> getTextConfigList() {
        return textConfigList;
    }

    public void setTextConfigList(RealmList<TextConfigBean> textConfigList) {
        this.textConfigList = textConfigList;
    }


}
