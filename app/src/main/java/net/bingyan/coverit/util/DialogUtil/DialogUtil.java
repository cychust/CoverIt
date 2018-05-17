package net.bingyan.coverit.util.DialogUtil;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import net.bingyan.coverit.util.DialogUtil.base.BaseDialogFragment;
import net.bingyan.coverit.util.DialogUtil.base.BindViewHolder;
import net.bingyan.coverit.util.DialogUtil.base.TController;
import net.bingyan.coverit.util.DialogUtil.listener.OnBindViewListener;
import net.bingyan.coverit.util.DialogUtil.listener.OnViewClickListener;

public class DialogUtil extends BaseDialogFragment {

    private static final String KEY_TCONTROLLER = "TController";

    protected TController tController;
    public DialogUtil(){
        tController=new TController();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            tController=(TController)savedInstanceState.getSerializable(KEY_TCONTROLLER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_TCONTROLLER,tController);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        DialogInterface.OnDismissListener onDismissListener=tController.getOnDismissListener();
        if (onDismissListener!=null){
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    protected int getLayoutRes() {
        return tController.getLayoutRes();
    }

    @Override
    protected View getDialogView() {
        return tController.getDialogView();
    }

    @Override
    protected void bindView(View view) {
        BindViewHolder viewHolder=new BindViewHolder(view,this);
        if (tController.isCancelable()&&tController.getIds()!=null&&tController.getIds().length>0){
            for (int id:tController.getIds()){
                viewHolder.addOnClickListener(id);
            }
        }
        if (tController.getOnBindViewListener()!=null){
            tController.getOnBindViewListener().bindView(viewHolder);
        }
    }

    @Override
    public int getGravity() {
        return tController.getGravity();
    }

    @Override
    public float getDimAmount() {
        return tController.getDimAmount();
    }

    @Override
    public int getDialogHeight() {
        return tController.getHeight();
    }

    @Override
    public int getDialogWidth() {
        return tController.getWidth();
    }

    @Override
    public String getFragmentTag() {
        return tController.getTag();
    }


    public OnViewClickListener getOnViewClickListener() {
        return tController.getOnViewClickListener();
    }

    @Override
    public boolean isCancelable() {
        return tController.isCancelable();
    }


    @Override
    protected boolean isCancelableOutside() {
        return tController.isCancelableOutside();
    }

    public DialogUtil show(){
        if (tController.getWidth()<=0&&tController.getHeight()<=0){
            tController.setWidth(600);
        }
        show(tController.getFragmentManager());
        return this;
    }
    public static class Builder {

        TController.TParams params;

        public Builder(FragmentManager fragmentManager) {
            params = new TController.TParams();
            params.mFragmentManager = fragmentManager;
        }

        //各种setXXX()方法设置数据
        public Builder setLayoutRes(@LayoutRes int layoutRes) {
            params.mLayoutRes = layoutRes;
            return this;
        }

        public Builder setDialogView(View view) {
            params.mDialogView = view;
            return this;
        }

        /**
         * 设置弹窗宽度是屏幕宽度的比例 0 -1
         */
        public Builder setScreenWidthAspect(Activity activity, float widthAspect) {
            params.mWidth = (int) (getWindowWidth(activity) * widthAspect);
            return this;
        }

        public Builder setWidth(int widthPx) {
            params.mWidth = widthPx;
            return this;
        }

        /**
         * 设置屏幕高度比例 0 -1
         */
        public Builder setScreenHeightAspect(Activity activity, float heightAspect) {
            params.mHeight = (int) (getWindowHeight(activity) * heightAspect);
            return this;
        }

        public Builder setHeight(int heightPx) {
            params.mHeight = heightPx;
            return this;
        }

        public Builder setGravity(int gravity) {
            params.mGravity = gravity;
            return this;
        }

        public Builder setCancelableOutside(boolean cancel) {
            params.mIsCancelableOutside = cancel;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            params.mCancelable = cancelable;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
            params.mOnDismissListener = dismissListener;
            return this;
        }


        public Builder setDimAmount(float dim) {
            params.mDimAmount = dim;
            return this;
        }

        public Builder setTag(String tag) {
            params.mTag = tag;
            return this;
        }

        public Builder setOnBindViewListener(OnBindViewListener listener) {
            params.bindViewListener = listener;
            return this;
        }

        public Builder addOnClickListener(int... ids) {
            params.ids = ids;
            return this;
        }

        public Builder setOnViewClickListener(OnViewClickListener listener) {
            params.mOnViewClickListener = listener;
            return this;
        }

        public DialogUtil create() {
            DialogUtil dialog = new DialogUtil();
            // Log.d(TAG, "create");
            //将数据从Buidler的DjParams中传递到DjDialog中
            params.apply(dialog.tController);
            return dialog;
        }
    }
}