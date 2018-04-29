package net.bingyan.coverit.ui.reciteother

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
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
import net.bingyan.coverit.util.PhotoBitmapUtils
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

    private var removeView: ModifyPicView? =null
    private lateinit var coverView: ModifyPicView

    private var canModify = true

    private lateinit var bitmap: Bitmap

    private lateinit var picRealm: Realm

    private lateinit var picItem:RecitePicBean

    private lateinit var resultText: String

    private lateinit var reciteBookResults: RealmResults<ReciteBookBean>

    private lateinit var pvCustomOptions: OptionsPickerView<ReciteBookBean>
    private lateinit var lcPic:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_pic)
        if (intent.getStringExtra("pic") != null)
            picPath = intent.getStringExtra("pic")
        picPath = PhotoBitmapUtils.amendRotatePhoto(picPath)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//A
        window.decorView.fitsSystemWindows=true

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
        lcPic=findViewById(R.id.cl_pic)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle("图片记背")
        titleBar.setTitleColor(ContextCompat.getColor(this, R.color.title_white))

        rbModify.isChecked = true

        btnSave.onClick {
            //addPicTitle()
            resultText = ""//todo
            reciteBookResults = picRealm.where(ReciteBookBean::class.java)
                    .findAll()
            initCustomOptionPicker()
            pvCustomOptions.show()
        }

        rbSwitch.setOnCheckedChangeListener(this)
        rbSee.setOnCheckedChangeListener(this)
        rbModify.setOnCheckedChangeListener(this)

        val myOptions = BitmapFactory.Options()
        myOptions.inPreferredConfig = Bitmap.Config.RGB_565

        bitmap = BitmapFactory.decodeFile(picPath,myOptions)
        LogUtil.d("the size is ${bitmap.byteCount}")
        LogUtil.d("the width is ${bitmap.width}")
        LogUtil.d("the height is ${bitmap.height}")

        Glide.with(this).load(bitmap).into(picture)

    if(intent.getSerializableExtra("picData")!=null){
        val redDataList=intent.getSerializableExtra("picData") as MutableList<PicConfigBean>

        redDataList.forEach {
            coverView = ModifyPicView(context, picPath)
            viewList.add(coverView)
            picFrame.addView(coverView)
            coverView.rectLeft = it.left
            coverView.rectTop = it.top
            coverView.rectDown = it.bottom
            coverView.rectRight = it.right
            coverView.setCanClick(true)
            coverView.setThisActivity(this)
            coverView.invalidate()
        }
    }

        class PictureListener : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if (canModify) {
                    val preWidTimes = bitmap.width.toDouble() / picture.width.toDouble()
                    val preHeiTimes = bitmap.height.toDouble() / picture.height.toDouble()
                    val freeHeiSpace=(picture.height-bitmap.height/preWidTimes)/2
                    val freeWidSpace=(picture.width-bitmap.width/preHeiTimes)/2
                    val action = p1?.action
                    if (isNewRect) {
                        coverView = ModifyPicView(context, picPath)
                        coverView.setThisActivity(this@ModifyPicActivity)
                        viewList.add(coverView)

                        picFrame.addView(coverView)
                    }
                    when (action) {
                        MotionEvent.ACTION_DOWN -> {
                            isNewRect = false

                            beginX = p1.x
                            beginY = p1.y

                            coverView.rectLeft = beginX
                            coverView.rectTop = beginY
                            coverView.rectDown = beginY
                            coverView.rectRight = beginX

                            coverView.invalidate()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            isNewRect = false

                            moveX = p1.x
                            moveY = p1.y

                            coverView.setMove(false)

                            coverView.rectTop = beginY
                            coverView.rectLeft = beginX
                            coverView.rectRight = moveX
                            coverView.rectDown = moveY

                            coverView.invalidate()
                        }
                        MotionEvent.ACTION_UP -> {
                            isNewRect = true
                            coverView.setCanClick(true)
                            coverView.setMove(true)
                            coverView.invalidate()
                            coverView.setMove(false)

                            if(preWidTimes>=preHeiTimes){
                                coverView.rectTop = when{
                                    beginY-freeHeiSpace<0->freeHeiSpace
                                    beginY+freeHeiSpace>picture.height->picture.height-freeHeiSpace
                                    else->beginY.toDouble()
                                }.toFloat()
                                coverView.rectLeft = beginX
                                coverView.rectRight = moveX
                                coverView.rectDown = when{
                                    moveY-freeHeiSpace<0->freeHeiSpace
                                    moveY+freeHeiSpace>picture.height->picture.height-freeHeiSpace
                                    else->moveY.toDouble()
                                }.toFloat()
                            }else{
                                coverView.rectTop = beginY
                                coverView.rectLeft = when{
                                    beginX-freeWidSpace<0->freeWidSpace
                                    beginX+freeWidSpace>picture.width->picture.width-freeWidSpace
                                    else->beginX.toDouble()
                                }.toFloat()
                                coverView.rectRight = when{
                                    moveX-freeWidSpace<0->freeWidSpace
                                    moveX+freeWidSpace>picture.width->picture.width-freeWidSpace
                                    else->moveX.toDouble()
                                }.toFloat()
                                coverView.rectDown = moveY
                            }
                            LogUtil.d(coverView.rectTop.toString()+"\n"+coverView.rectLeft.toString()+"\n"+coverView.rectRight.toString()+"\n"+coverView.rectDown.toString())
                            coverView.invalidate()
                        }
                    }
                    return true
                } else return false
            }
        }

        val listener = PictureListener()
        picture.setOnTouchListener(listener)
    }

    fun removeView(view: ModifyPicView) {
        viewList.remove(view)
        picFrame.removeView(view)
        picture.invalidate()
    }

    private fun initCustomOptionPicker() {
        pvCustomOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, _, _, _ ->
            savePic()
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
        val builder = AlertDialog.Builder(this)
        builder.setTitle("创建记背本")

        val dialogContent = layoutInflater.inflate(R.layout.custom_dialog, null)
        builder.setView(dialogContent)

        val textInput = dialogContent.findViewById<TextInputEditText>(R.id.input_text)
        val textInputLayout = dialogContent.findViewById<TextInputLayout>(R.id.input_text_layout)
        textInputLayout.hint = "请输入记背本名称"

        builder.setCancelable(false)


        builder.setPositiveButton("确定", null)
        builder.setNegativeButton("取消", { dialog, which ->
            run {
                resultText = ""
                dialog.dismiss()
                pvCustomOptions.show()
            }
        })
        val dialog = builder.create()
        //dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog.show()
        if(dialog.getButton(AlertDialog.BUTTON_POSITIVE)!=null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                resultText = textInput.text.toString()
                if (!resultText.trim().isEmpty()) {
                    if(picRealm.where(ReciteBookBean::class.java).equalTo("bookTitle",resultText).findAll().isEmpty()){
                        picRealm.beginTransaction()
                        val bookItem = picRealm.createObject(ReciteBookBean::class.java)
                        bookItem.bookTitle = resultText
                        bookItem.textNum = 0
                        bookItem.picNum = 0
                        bookItem.isTop = false
                        bookItem.bookDate = Date(System.currentTimeMillis())
                        picRealm.commitTransaction()
                    }else {
                        textInput.error="记背本已存在,不能重复创建!"
                        return@setOnClickListener
                    }
                }else {
                    textInput.error = "记背本名称不能为空!"
                    return@setOnClickListener
                }
                pvCustomOptions.setPicker(picRealm.copyFromRealm(reciteBookResults))
                dialog.dismiss()
                pvCustomOptions.setSelectOptions(reciteBookResults.size-1)
                pvCustomOptions.show()
            }
        }
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
                }else Toast.makeText(this,"图片名称不能为空!",Toast.LENGTH_SHORT).show()
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
        picItem.picPath = FileUtils.saveBitmap(bitmap)

        for (modifyPicView:ModifyPicView in viewList){
            val picConfig=PicConfigBean()
            picConfig.left=modifyPicView.rectLeft
            picConfig.top=modifyPicView.rectTop
            picConfig.right=modifyPicView.rectRight
            picConfig.bottom =modifyPicView.rectDown
            picItem.picConfigList.add(picConfig)
        }

        picRealm.commitTransaction()
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
                        val preWidTimes = bitmap.width.toDouble() / picture.width.toDouble()
                        val preHeiTimes = bitmap.height.toDouble() / picture.height.toDouble()
                        if(preWidTimes>=preHeiTimes){
                            val freeHeiSpace=(picture.height-bitmap.height/preWidTimes)/2
                            view.setCalRectLeft(view.rectLeft)
                            view.setCalRectTop(when{
                                view.rectTop-freeHeiSpace<0->freeHeiSpace
                                view.rectTop+freeHeiSpace>picture.height->picture.height-freeHeiSpace
                                else->view.rectTop-freeHeiSpace
                            }.toFloat())
                            view.setCalRectRight(view.rectRight)
                            view.setCalRectDown(when{
                                view.rectDown-freeHeiSpace<0->freeHeiSpace
                                view.rectDown+freeHeiSpace>picture.height->picture.height-freeHeiSpace
                                else->view.rectDown-freeHeiSpace
                            }.toFloat())
                            view.setWidTimes(preWidTimes)
                            view.setHeiTimes(preWidTimes)
                        }else{
                            val freeWidSpace=(picture.width-bitmap.width/preHeiTimes)/2
                            view.setCalRectLeft(when{
                                view.rectLeft-freeWidSpace<0->freeWidSpace
                                view.rectLeft+freeWidSpace>picture.width->picture.width-freeWidSpace
                                else->view.rectLeft-freeWidSpace
                            }.toFloat())
                            view.setCalRectTop(view.rectTop)
                            view.setCalRectRight(when{
                                view.rectRight-freeWidSpace<0->freeWidSpace
                                view.rectRight+freeWidSpace>picture.width->picture.width-freeWidSpace
                                else->view.rectRight-freeWidSpace
                            }.toFloat())
                            view.setCalRectDown(view.rectDown)
                            view.setWidTimes(preHeiTimes)
                            view.setHeiTimes(preHeiTimes)
                        }
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
                    for (view: ModifyPicView in this.viewList) {
                       view.setCanModify(true)
                    }
                    canModify = true
                }
                if (!p1) {
                    for (view: ModifyPicView in this.viewList) {
                        view.setCanModify(false)
                    }
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
