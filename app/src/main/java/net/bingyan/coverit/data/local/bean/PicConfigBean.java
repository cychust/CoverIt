package net.bingyan.coverit.data.local.bean;

import io.realm.RealmObject;

/**
 * Author       zdlly
 * Date         2018.3.28
 * Time         23:01
 */

public class PicConfigBean extends RealmObject {
    private int left;
    private int top;
    private int right;
    private int bottom;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }
}
