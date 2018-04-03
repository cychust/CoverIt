package net.bingyan.coverit.ui.reciteother

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bumptech.glide.Glide
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_modify_pic.*
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.PicConfigBean
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.data.local.bean.RecitePicBean
import net.bingyan.coverit.util.FileUtils
import net.bingyan.coverit.util.LogUtil
import net.bingyan.coverit.widget.ModifyPicView
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class ModifyPicActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var btnSave: Button

    private lateinit var picFrame: FrameLayout
    private lateinit var picture: AppCompatImageView
    private lateinit var rbSwitch: CheckBox
    private lateinit var rbSee: CheckBox
    private lateinit var rbModify: CheckBox
    private lateinit var picPath: String

    private val context: Context = this
    private var beginX = 0f
    private var beginY = 0f
    private var moveX: Float = 0.toFloat()
    private var moveY: Float = 0.toFloat()

    private var isNewRect: Boolean = true

    private val viewList = mutableListOf<ModifyPicView>()

    private val TAG = "PicView"
    private var coverView: ModifyPicView? = null
    private var oldCoverView: ModifyPicView? = null

    private var canModify = true

    private lateinit var bitmap: Bitmap

    private lateinit var picRealm: Realm

    private lateinit var picItem:RecitePicBean

    private lateinit var resultText: String

    private lateinit var reciteBookResults: RealmResults<ReciteBookBean>

    private lateinit var pvCustomOptions: OptionsPickerView<ReciteBookBean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_pic)
        if (intent.getStringExtra("pic") != null)
            picPath = intent.getStringExtra("pic")
        picRealm= Realm.getDefaultInstance()
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
            addPicTitle()
        }

        rbSwitch.setOnCheckedChangeListener(this)
        rbSee.setOnCheckedChangeListener(this)
        rbModify.setOnCheckedChangeListener(this)

        //val myOptions = BitmapFactory.Options()
        //myOptions.inPreferredConfig = Bitmap.Config.ARGB_4444
        //myOptions.inSampleSize=2

        bitmap = BitmapFactory.decodeFile(picPath)
        LogUtil.d("the size is ${bitmap.byteCount}")
        LogUtil.d("the width is ${bitmap.width}")
        LogUtil.d("the height is ${bitmap.height}")
        Glide.with(this).load(bitmap).into(picture)


        class PictureListener : View.OnTouchListener {

            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if (canModify) {
                    val action = p1?.action
                    if (isNewRect) {
                        coverView = ModifyPicView(context, picPath)
                        viewList.add(coverView!!)
                        Log.d(TAG, "onTouch: view created!")
                        picFrame.addView(coverView)
                    }
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

                            Log.d(TAG, "onTouch: left$beginX")
                            Log.d(TAG, "onTouch: top$beginY")
                            coverView!!.invalidate()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            isNewRect = false
                            Log.d(TAG, "onTouch: begin move")


                            moveX = p1.x
                            moveY = p1.y


                            Log.d(TAG, "onTouch: beginx$beginX")
                            Log.d(TAG, "onTouch: beginy$beginY")
                            coverView!!.setMove(false)
                            coverView!!.rectTop = beginY
                            coverView!!.rectLeft = beginX
                            coverView!!.rectRight = moveX
                            coverView!!.rectDown = moveY
                            coverView!!.invalidate()

                        }
                        MotionEvent.ACTION_UP -> {
                            isNewRect = true
                            Log.d(TAG, "onTouch: begin up")
                            coverView!!.setCanClick(true)
                            coverView!!.setMove(true)
                            coverView!!.invalidate()
                            coverView!!.setMove(false)
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

        val listener = PictureListener()
        picture.setOnTouchListener(listener)
    }

    private fun initCustomOptionPicker() {
        pvCustomOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, _, _, _ ->
            val selectedItem=reciteBookResults[options1]
            picRealm.executeTransaction {
                picItem.belonging=selectedItem!!.pickerViewText
            }
            picRealm.executeTransaction({
                //先查找后得到对象
                val user = picRealm.where(ReciteBookBean::class.java).equalTo("bookTitle",selectedItem!!.pickerViewText).findFirst()
                user!!.picNum +=1
                user!!.picList.add(picItem)
                user.bookDate= Date(System.currentTimeMillis())
                Toast.makeText(this,"已成功添加",Toast.LENGTH_SHORT).show()
                finish()
            })
        })
                .setLayoutRes(R.layout.pickerview_custom_options) { v ->
                    val tvSubmit = v.findViewById(R.id.tv_finish) as TextView
                    val tvAdd = v.findViewById(R.id.tv_add) as TextView
                    val ivCancel = v.findViewById(R.id.iv_cancel) as ImageView
                    tvSubmit.setOnClickListener {
                        pvCustomOptions.returnData()
                        pvCustomOptions.dismiss()
                    }

                    ivCancel.setOnClickListener { pvCustomOptions.dismiss() }

                    tvAdd.setOnClickListener {
                        pvCustomOptions.dismiss()
                        addNewReciteBook()
                    }
                }
                .isDialog(true)
                .build()

        pvCustomOptions.setPicker(picRealm.copyFromRealm(reciteBookResults))//添加数据
    }

    private fun addNewReciteBook() {
        val builder=AlertDialog.Builder(this)
        builder.setTitle("创建记背本")

        val dialogContent=layoutInflater.inflate(R.layout.custom_dialog,null)
        builder.setView(dialogContent)

        val textInput=dialogContent.findViewById<TextInputEditText>(R.id.input_text)
        textInput.hint="请输入记背本名称"

        builder.setCancelable(false)


        builder.setPositiveButton("确定", { dialog, which ->
            run {
                resultText = textInput.text.toString()
                if (!resultText.trim().isEmpty()){
                    picRealm.beginTransaction()
                    val bookItem=picRealm.createObject(ReciteBookBean::class.java)
                    bookItem.bookTitle=resultText
                    bookItem.textNum=0
                    bookItem.picNum=0
                    bookItem.isTop=false
                    bookItem.bookDate= Date(System.currentTimeMillis())
                    picRealm.commitTransaction()
                }
                pvCustomOptions.setPicker(picRealm.copyFromRealm(reciteBookResults))
                dialog.dismiss()
                pvCustomOptions.setSelectOptions(reciteBookResults.size-1)
                pvCustomOptions.show()
            }
        })
        builder.setNegativeButton("取消", { dialog, which -> run {
            resultText = ""
            dialog.dismiss()
            pvCustomOptions.show()
        } })
        val dialog=builder.create()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        dialog.show()
    }


    private fun addPicTitle() {
        val builder= AlertDialog.Builder(this)
        builder.setTitle("给这张图片起个名字吧")

        val dialogContent=layoutInflater.inflate(R.layout.custom_dialog,null)
        builder.setView(dialogContent)

        val textInput=dialogContent.findViewById<TextInputEditText>(R.id.input_text)

        textInput.hint = "添加图片名称"

        builder.setCancelable(false)


        builder.setPositiveButton("确定", { dialog, which ->
            run {
                resultText = textInput.text.toString()
                if (!resultText.trim().isEmpty()){
                    dialog.dismiss()
                    savePic()
                }else textInput.error = "内容不能为空！"
            }
        })
        builder.setNegativeButton("取消", { dialog, which -> run {
            dialog.dismiss()
        } })
        val dialog=builder.create()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        dialog.show()
    }

    private fun savePic() {
        picRealm.beginTransaction()
        picItem=picRealm.createObject(RecitePicBean::class.java)
        picItem.picTitle=resultText
        picItem.isTop=false
        picItem.picDate= Date(System.currentTimeMillis())
        picItem.picPath=FileUtils.saveBitmap(bitmap,this)

        for (modifyPicView:ModifyPicView in viewList){
            val picConfig=PicConfigBean()
            picConfig.left=modifyPicView.rectLeft
            picConfig.top=modifyPicView.rectTop
            picConfig.right=modifyPicView.rectRight
            picConfig.bottom =modifyPicView.rectDown
            picItem.picConfigList.add(picConfig)
        }

        picRealm.commitTransaction()
        reciteBookResults=picRealm.where(ReciteBookBean::class.java)
                .findAll()
        initCustomOptionPicker()
        pvCustomOptions.show()
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
                    canModify = true
                }
                if (!p1) {
                    canModify = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        picRealm.close()
    }
}
