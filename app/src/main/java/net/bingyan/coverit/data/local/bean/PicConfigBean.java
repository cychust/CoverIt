package net.bingyan.coverit.data.local.bean;

import io.realm.RealmObject;

/**
 * Author       zdlly
 * Date         2018.3.28
 * Time         23:01
 */

public class PicConfigBean extends RealmObject {
    private float left;
    private float top;
    private float right;
    private float bottom;

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
