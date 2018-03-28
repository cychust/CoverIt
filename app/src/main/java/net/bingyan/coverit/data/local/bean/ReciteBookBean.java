package net.bingyan.coverit.data.local.bean;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Author       zdlly
 * Date         2018.3.28
 * Time         22:43
 */

public class ReciteBookBean extends RealmObject {
    private String bookTitle;
    private int textNum;
    private int picNum;
    private Date bookDate;
    private boolean isTop;
    private RealmList<ReciteTextBean> textList;
    private RealmList<RecitePicBean> picList;

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getTextNum() {
        return textNum;
    }

    public void setTextNum(int textNum) {
        this.textNum = textNum;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public RealmList<ReciteTextBean> getTextList() {
        return textList;
    }

    public void setTextList(RealmList<ReciteTextBean> textList) {
        this.textList = textList;
    }

    public RealmList<RecitePicBean> getPicList() {
        return picList;
    }

    public void setPicList(RealmList<RecitePicBean> picList) {
        this.picList = picList;
    }
}
