package net.bingyan.coverit.widget

import android.app.Activity
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import net.bingyan.coverit.R
import net.bingyan.coverit.ui.reciteother.CreateTextActivity
import org.jetbrains.anko.intentFor
import razerdp.basepopup.BasePopupWindow


/**
 * Author       zdlly
 * Date         2018.4.3
 * Time         21:29
 */
class MenuPopup(val context: Activity) : BasePopupWindow(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT), View.OnClickListener {

    private lateinit var addNewTextLayout: LinearLayout
    private lateinit var addNewPicLayout: LinearLayout

    private lateinit var popView: View

    init {
        popView.findViewById<View>(R.id.ll_new_text).setOnClickListener(this)
        popView.findViewById<View>(R.id.ll_new_pic).setOnClickListener(this)
    }

    override fun initShowAnimation(): Animation {
        val set = AnimationSet(true)
        set.interpolator = DecelerateInterpolator()
        set.addAnimation(getScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f))
        set.addAnimation(defaultAlphaAnimation)
        return set
        //return null;
    }

    override fun initExitAnimation(): Animation {
        val set = AnimationSet(true)
        set.interpolator = DecelerateInterpolator()
        set.addAnimation(getScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f))
        set.addAnimation(getDefaultAlphaAnimation(false))
        return set
    }

    override fun showPopupWindow(v: View) {
        offsetX = -(width - v.width)
        //offsetY = -v.height / 2
        super.showPopupWindow(v)
    }

    override fun getClickToDismissView(): View? {
        return null
    }

    override fun onCreatePopupView(): View {
        popView = createPopupById(R.layout.popup_menu)
        return popView
    }

    override fun initAnimaView(): View {
        return popupWindowView.findViewById(R.id.popup_container)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_new_text -> {
                startActivity(context, context.intentFor<CreateTextActivity>(), null)
                this.dismiss()
            }
            R.id.ll_new_pic -> {
                val popup = SlideFromBottomPopup(context,context)
                popup.showPopupWindow()
                this.dismiss()
            }
            else -> {
            }
        }

    }
}