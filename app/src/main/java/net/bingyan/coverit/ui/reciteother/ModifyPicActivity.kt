package net.bingyan.coverit.ui.reciteother

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_modify_pic.*
import net.bingyan.coverit.R
import net.bingyan.coverit.widget.ModifyPicView
import org.jetbrains.anko.sdk25.coroutines.onClick

class ModifyPicActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var btnSave: Button

    private lateinit var picFrame: FrameLayout
    private lateinit var picture: AppCompatImageView
    private lateinit var rbSwitch: CheckBox
    private lateinit var rbSee: CheckBox
    private lateinit var rbModify: CheckBox
    private lateinit var picPath: String

    private val context:Context=this
    private var beginX = 0f
    private var beginY = 0f
    private var moveX: Float = 0.toFloat()
    private var moveY: Float = 0.toFloat()
    private var isNewRect: Boolean = false

    private val viewList = mutableListOf<ModifyPicView>()

    private val TAG = "PicView"
    private var coverView: ModifyPicView? = null
    private var oldCoverView: ModifyPicView? = null

    private var canModify = true

    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_pic)
        picPath = intent.getStringExtra("pic")
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        btnSave = findViewById(R.id.btn_save)
        picFrame = findViewById(R.id.picture_frame)
        picture = findViewById(R.id.picture)
        rbSwitch = findViewById(R.id.switch_button)
        rbSee = findViewById(R.id.see_button)
        rbModify = findViewById(R.id.modify_button)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle("图片记背")
        titleBar.setTitleColor(ContextCompat.getColor(this, R.color.title_white))

        rbModify.isChecked = true

        btnSave.onClick {

        }

        rbSwitch.setOnCheckedChangeListener(this)
        rbSee.setOnCheckedChangeListener(this)
        rbModify.setOnCheckedChangeListener(this)

        bitmap = BitmapFactory.decodeFile(picPath)
        picture.setImageBitmap(bitmap)


        class PictureListener: View.OnTouchListener {

            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if (canModify) {
                    val action = p1?.action
                    if (!isNewRect) {
                        oldCoverView = coverView
                        picFrame.removeView(oldCoverView)
                        viewList.remove(oldCoverView)
                        Log.d(TAG, "onTouch: view removed!")
                    }
                    coverView = ModifyPicView(context, picPath)
                    viewList.add(coverView!!)
                    Log.d(TAG, "onTouch: view created!")
                    picFrame.addView(coverView)
                    when (action) {
                        MotionEvent.ACTION_DOWN -> {
                            isNewRect = false
                            Log.d(TAG, "onTouch: begin down")
                            beginX = p1.x
                            beginY = p1.y
                            coverView!!.rectLeft = beginX
                            coverView!!.rectTop = beginY
                            coverView!!.rectDown = beginY
                            coverView!!.rectRight = beginX

                            Log.d(TAG, "onTouch: left" + beginX)
                            Log.d(TAG, "onTouch: top" + beginY)
                            coverView!!.invalidate()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            isNewRect = false
                            Log.d(TAG, "onTouch: begin move")
                            moveX = p1.x
                            moveY = p1.y
                            Log.d(TAG, "onTouch: beginx" + beginX)
                            Log.d(TAG, "onTouch: beginy" + beginY)

                            coverView!!.rectTop = beginY
                            coverView!!.rectLeft = beginX
                            coverView!!.rectRight = moveX
                            coverView!!.rectDown = moveY
                            Log.d(TAG, "onTouch: recleft" + coverView!!.rectLeft)
                            Log.d(TAG, "onTouch: recright" + coverView!!.rectRight)
                            Log.d(TAG, "onTouch: rectop" + coverView!!.rectTop)
                            Log.d(TAG, "onTouch: recdown" + coverView!!.rectDown)
                            coverView!!.invalidate()
                        }
                        MotionEvent.ACTION_UP -> {
                            isNewRect = true
                            Log.d(TAG, "onTouch: begin up")
                            coverView!!.setCanClick(true)
                            coverView!!.rectTop = beginY
                            coverView!!.rectLeft = beginX
                            coverView!!.rectRight = moveX
                            coverView!!.rectDown = moveY
                            coverView!!.invalidate()
                        }
                    }
                    return true
                } else return false
            }
        }
        val listener=PictureListener()
        picture.setOnTouchListener(listener)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0?.id) {
            R.id.switch_button -> {
                if (!p1) {
                    for (view: ModifyPicView in this.viewList) {
                        view.setSwitch(false)
                        view.invalidate()
                    }
                    picture.setImageBitmap(bitmap)

                }
                if (p1) {
                    for (view: ModifyPicView in this.viewList) {
                        view.setSwitch(true)
                        val widTimes = bitmap.width.toDouble() / picture.width.toDouble()
                        val heiTimes = bitmap.height.toDouble() / picture.height.toDouble()
                        view.setWidTimes(widTimes)
                        view.setHeiTimes(heiTimes)
                        view.invalidate()
                    }
                    picture.setImageDrawable(ColorDrawable(Color.WHITE))
                }
            }
            R.id.see_button -> {
                if (p1) {
                    for (view: View in this.viewList) {
                        view.alpha = 0.5f
                        view.invalidate()
                    }
                }
                if (!p1) {
                    for (view: View in this.viewList) {
                        view.alpha = 1.0f
                        view.invalidate()
                    }
                }
            }
            R.id.modify_button -> {
                if (p1) {
                    canModify=true
                }
                if (!p1) {
                    canModify=false
                }
            }
        }
    }
}
