package net.bingyan.coverit.widget

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import net.bingyan.coverit.R
import net.bingyan.coverit.util.FileUtils
import razerdp.basepopup.BasePopupWindow
import java.io.File
import java.io.IOException

/**
 * Author       cychust
 * Date         2017.12.23
 * Time         19:37
 */


class SlideFromBottomPopup(activityContext: Activity,val activity: Activity) : BasePopupWindow(activityContext), View.OnClickListener {

    val TAKE_PHOTO=1
    val CHOOSE_PHOTO=2
    private lateinit var popupView:View

    init {
        bindEvent()
    }

    override fun initShowAnimation(): Animation {
        return getTranslateVerticalAnimation(1f, 0f, 500)
    }

    override fun initExitAnimation(): Animation {
        return getTranslateVerticalAnimation(0f,1f,500)
    }

    override fun getClickToDismissView(): View {
        return popupView.findViewById(R.id.click_to_dismiss)
    }

    override fun onCreatePopupView(): View? {
        popupView = LayoutInflater.from(context).inflate(R.layout.popup_slide_from_bottom, null)
        return popupView
    }

    override fun initAnimaView(): View {
        return popupView.findViewById(R.id.popup_anima)
    }

    private fun bindEvent() {
        popupView.findViewById<View>(R.id.take_photo).setOnClickListener(this)
        popupView.findViewById<View>(R.id.select_from_album).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.take_photo -> {
                val outputImage = File(activity.externalCacheDir, "output_image.jpg")
                try {
                    if (outputImage.exists()) {
                        outputImage.delete()
                    }
                    outputImage.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                FileUtils.startActionCapture(activity, outputImage, TAKE_PHOTO)
                this.dismiss()
            }
            R.id.select_from_album -> {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                }
                val intent1 = Intent("android.intent.action.GET_CONTENT")
                intent1.type = "image/*"
                activity.startActivityForResult(intent1, CHOOSE_PHOTO)
                this.dismiss()
            }

            else -> {
            }
        }

    }

}
