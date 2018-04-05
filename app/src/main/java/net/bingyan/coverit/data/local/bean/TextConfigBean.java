package net.bingyan.coverit.data.local.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Author       zdlly
 * Date         2018.3.28
 * Time         23:00
 */

public class TextConfigBean extends RealmObject implements Serializable {
    private int previous;
    private int next;

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
