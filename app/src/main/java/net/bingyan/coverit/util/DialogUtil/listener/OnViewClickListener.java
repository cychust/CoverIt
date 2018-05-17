package net.bingyan.coverit.util.DialogUtil.listener;

import android.view.View;

import net.bingyan.coverit.util.DialogUtil.DialogUtil;
import net.bingyan.coverit.util.DialogUtil.base.BindViewHolder;

public interface OnViewClickListener {
    void onViewClick(BindViewHolder viewHolder, View view, DialogUtil tDialog);
}
