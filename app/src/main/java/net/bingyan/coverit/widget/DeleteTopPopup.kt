package net.bingyan.coverit.widget

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import net.bingyan.coverit.R
import net.bingyan.coverit.listener.PopupCallBack
import razerdp.basepopup.BasePopupWindow

/**
 * Author       cychust
 * Date         2018.4.8
 * Time         0:04
 */
class DeleteTopPopup(val activityContext: Activity,val popupCallBack: PopupCallBack,val isTop:Boolean) : BasePopupWindow(activityContext), View.OnClickListener {
    /**
     * Author       zdlly
     * Date         2017.12.23
     * Time         19:37
     */

    private lateinit var popupView: View
    private lateinit var deleteTip:View
    private lateinit var topView:TextView

    init {
        bindEvent()
    }

    override fun initShowAnimation(): Animation {
        return getTranslateVerticalAnimation(1f, 0f, 500)
    }

    override fun initExitAnimation(): Animation {
        return getTranslateVerticalAnimation(0f, 1f, 500)
    }

    override fun getClickToDismissView(): View {
        return popupView.findViewById(R.id.click_to_dismiss)
    }

    override fun onCreatePopupView(): View? {
        popupView = LayoutInflater.from(context).inflate(R.layout.delete_top_popup, null)
        return popupView
    }

    override fun initAnimaView(): View {
        return popupView.findViewById(R.id.popup_anima)
    }

    public fun setTipVisibility(visibility:Int){
        deleteTip.visibility=visibility
    }
    private fun bindEvent() {
        topView= popupView.findViewById<View>(R.id.book_top) as TextView
        popupView.findViewById<View>(R.id.book_rename).setOnClickListener(this)
        popupView.findViewById<View>(R.id.book_delete).setOnClickListener(this)
        deleteTip=popupView.findViewById<View>(R.id.delete_tip)
        topView.setOnClickListener(this)
        if(isTop){
            topView.text = "取消置顶"
        }else topView.text = "置顶"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.book_top->{
                popupCallBack.onTopClicked()
            }
            R.id.book_rename->{
                popupCallBack.onRenameClicked()
            }
            R.id.book_delete->{
                popupCallBack.onDeleteClicked()
            }
        }

    }

}
