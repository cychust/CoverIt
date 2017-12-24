package net.bingyan.coverit.ui.reciteother

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_modify.*
import net.bingyan.coverit.R
import net.bingyan.coverit.widget.ModifyPicView
import org.jetbrains.anko.sdk25.coroutines.onClick

class ModifyPicActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var btnSave:Button

    private lateinit var picFrame:FrameLayout
    private lateinit var picture:AppCompatImageView
    private lateinit var rbSwitch:CheckBox
    private lateinit var rbSee:CheckBox
    private lateinit var rbModify:CheckBox
    private lateinit var picPath:String
    private var beginX = 0f
    private var beginY = 0f
    private var moveX: Float = 0.toFloat()
    private var moveY: Float = 0.toFloat()
    private var isNewRect: Boolean = false

    private val TAG="PicView"
    private var coverView: ModifyPicView? = null
    private var oldCoverView:ModifyPicView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)
        picPath=intent.getStringExtra("pic")
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        btnSave=findViewById(R.id.btn_save)
        picFrame=findViewById(R.id.picture_frame)
        picture=findViewById(R.id.picture)
        rbSwitch=findViewById(R.id.switch_button)
        rbSee=findViewById(R.id.see_button)
        rbModify=findViewById(R.id.modify_button)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle("图片记背")
        titleBar.setTitleColor(ContextCompat.getColor(this,R.color.title_white))

        btnSave.onClick {

        }

        rbSwitch.setOnCheckedChangeListener(this)
        rbSee.setOnCheckedChangeListener(this)
        rbModify.setOnCheckedChangeListener(this)

        val bitmap = BitmapFactory.decodeFile(picPath)
        picture.setImageBitmap(bitmap)

        picture.setOnTouchListener({ _, event ->
            val action = event.action
            if (!isNewRect) {
                oldCoverView = coverView
                picFrame.removeView(oldCoverView)
                Log.d(TAG, "onTouch: view removed!")
            }
            coverView = ModifyPicView(this)
            Log.d(TAG, "onTouch: view created!")
            picFrame.addView(coverView)
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    isNewRect = false
                    Log.d(TAG, "onTouch: begin down")
                    beginX = event.x
                    beginY = event.y
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
                    moveX = event.x
                    moveY = event.y
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
            true
        })
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when(p0?.id){
            R.id.switch_button->{

            }
            R.id.see_button->{

            }
            R.id.modify_button->{

            }
        }
    }
}
