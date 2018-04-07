package net.bingyan.coverit.widget

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import net.bingyan.coverit.R
import razerdp.basepopup.BasePopupWindow

/**
 * Author       zdlly
 * Date         2018.4.8
 * Time         0:04
 */
class DeleteTopPopup(activityContext: Activity) : BasePopupWindow(activityContext), View.OnClickListener {
    /**
     * Author       zdlly
     * Date         2017.12.23
     * Time         19:37
     */

    private lateinit var popupView: View

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

    private fun bindEvent() {
        popupView.findViewById<View>(R.id.book_top).setOnClickListener(this)
        popupView.findViewById<View>(R.id.book_rename).setOnClickListener(this)
        popupView.findViewById<View>(R.id.book_delete).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.book_top->{

            }
            R.id.book_rename->{

            }
            R.id.book_delete->{

            }
        }

    }

}
