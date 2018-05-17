package net.bingyan.coverit.util.DialogUtil.list;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.bingyan.coverit.R;
import net.bingyan.coverit.util.DialogUtil.DialogUtil;
import net.bingyan.coverit.util.DialogUtil.base.BaseAdapter;
import net.bingyan.coverit.util.DialogUtil.base.TController;
import net.bingyan.coverit.util.DialogUtil.listener.OnBindViewListener;
import net.bingyan.coverit.util.DialogUtil.listener.OnViewClickListener;


/**
 * Created by cyc20 on 2018/4/18.
 */

public class ListDialogUtil extends DialogUtil {
    @Override
    protected void bindView(View view) {
        super.bindView(view);
        if (tController.getAdapter() != null) {//有设置列表
            //列表
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            if (recyclerView == null) {
                throw new IllegalArgumentException("自定义列表xml布局,请设置RecyclerView的控件id为recycler_view");
            }
            tController.getAdapter().setDialog(this);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),tController.getOrientation(),false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(tController.getAdapter());
            tController.getAdapter().notifyDataSetChanged();
            if (tController.getAdapterItemClickListener() != null) {
                tController.getAdapter().setOnAdapterItemClickListener(tController.getAdapterItemClickListener());
            }
        }else{
            Log.d("TDialog","列表弹窗需要先调用setAdapter()方法!");
        }
    }

    /*********************************************************************
     * 使用Builder模式实现
     *
     */
    public static class Builder {

        TController.TParams params;

        public Builder(FragmentManager fragmentManager) {
            params = new TController.TParams();
            params.mFragmentManager = fragmentManager;
        }

        //各种setXXX()方法设置数据
        public ListDialogUtil.Builder setLayoutRes(@LayoutRes int layoutRes) {
            params.mLayoutRes = layoutRes;
            return this;
        }

        //设置自定义列表布局和方向
        public ListDialogUtil.Builder setListLayoutRes(@LayoutRes int layoutRes,int orientation) {
            params.listLayoutRes = layoutRes;
            params.orientation = orientation;
            return this;
        }

        /**
         * 设置弹窗宽度是屏幕宽度的比例 0 -1
         */
        public ListDialogUtil.Builder setScreenWidthAspect(Activity activity, float widthAspect) {
            params.mWidth = (int) (getWindowWidth(activity) * widthAspect);
            return this;
        }

        public ListDialogUtil.Builder setWidth(int widthPx) {
            params.mWidth = widthPx;
            return this;
        }

        /**
         * 设置屏幕高度比例 0 -1
         */
        public ListDialogUtil.Builder setScreenHeightAspect(Activity activity, float heightAspect) {
            params.mHeight = (int) (getWindowHeight(activity) * heightAspect);
            return this;
        }

        public ListDialogUtil.Builder setHeight(int heightPx) {
            params.mHeight = heightPx;
            return this;
        }

        public ListDialogUtil.Builder setGravity(int gravity) {
            params.mGravity = gravity;
            return this;
        }

        public ListDialogUtil.Builder setCancelOutside(boolean cancel) {
            params.mIsCancelableOutside = cancel;
            return this;
        }

        public ListDialogUtil.Builder setDimAmount(float dim) {
            params.mDimAmount = dim;
            return this;
        }

        public ListDialogUtil.Builder setTag(String tag) {
            params.mTag = tag;
            return this;
        }

        public ListDialogUtil.Builder setOnBindViewListener(OnBindViewListener listener) {
            params.bindViewListener = listener;
            return this;
        }

        public ListDialogUtil.Builder addOnClickListener(int... ids) {
            params.ids = ids;
            return this;
        }

        public ListDialogUtil.Builder setOnViewClickListener(OnViewClickListener listener) {
            params.mOnViewClickListener = listener;
            return this;
        }

        //列表数据,需要传入数据和Adapter,和item点击数据
        public <A extends BaseAdapter> ListDialogUtil.Builder setAdapter(A adapter) {
            params.adapter = adapter;
            return this;
        }

        public ListDialogUtil.Builder setOnAdapterItemClickListener(BaseAdapter.OnAdapterItemClickListener listener) {
            params.adapterItemClickListener = listener;
            return this;
        }

        public ListDialogUtil.Builder setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
            params.mOnDismissListener = dismissListener;
            return this;
        }

        public ListDialogUtil create() {
            ListDialogUtil dialog = new ListDialogUtil();
            //将数据从Buidler的DjParams中传递到DjDialog中
            params.apply(dialog.tController);
            return dialog;
        }
    }
}
