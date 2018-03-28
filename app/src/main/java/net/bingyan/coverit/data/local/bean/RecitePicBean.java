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
}
