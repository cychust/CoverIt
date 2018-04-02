package net.bingyan.coverit.ui.reciteother

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.WindowManager
import android.widget.*
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_modify_pic.*
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.data.local.bean.ReciteTextBean
import net.bingyan.coverit.data.local.bean.TextConfigBean
import net.bingyan.coverit.data.local.dataadapter.RedData
import net.bingyan.coverit.widget.ModifyTextView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import kotlin.collections.ArrayList




class ModifyTextActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var btnSave: Button

    private lateinit var modifyText: ModifyTextView
    private lateinit var cbSwitch: CheckBox
    private lateinit var cbSee: CheckBox
    private lateinit var cbWrite: CheckBox
    private lateinit var cbModify: CheckBox
    private lateinit var title: String

    private lateinit var content: String

    private lateinit var pvCustomOptions:OptionsPickerView<ReciteBookBean>

    private var redList = ArrayList<RedData>()

    private lateinit var textRealm:Realm
    private lateinit var reciteBookResults: RealmResults<ReciteBookBean>

    private lateinit var resultText:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_text)
        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")

        textRealm= Realm.getDefaultInstance()
        initView()
    }

    private fun initView() {
        btnSave = findViewById(R.id.btn_save)
        modifyText = findViewById(R.id.modify_text)
        cbSwitch = findViewById(R.id.switch_button)
        cbSee = findViewById(R.id.see_button)
        cbWrite = findViewById(R.id.write_button)
        cbModify = findViewById(R.id.modify_button)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle(title)
        titleBar.setTitleColor(ContextCompat.getColor(this, R.color.title_white))

        cbSwitch.setOnCheckedChangeListener(this)
        cbSee.setOnCheckedChangeListener(this)
        cbWrite.setOnCheckedChangeListener(this)
        cbModify.setOnCheckedChangeListener(this)

        cbWrite.isChecked=false
        cbSee.isChecked=true
        cbModify.isChecked=true
        cbSwitch.isChecked=false


        modifyText.setText(content)
        modifyText.backgroundColor = Color.WHITE
        modifyText.isCursorVisible=false
        modifyText.movementMethod=ScrollingMovementMethod()
        modifyText.isLongClickable=false

        modifyText.drawBlack()

        btnSave.onClick {
            saveText()
            reciteBookResults=textRealm.where(ReciteBookBean::class.java)
                    .findAll()
            initCustomOptionPicker()
            pvCustomOptions.show()
        }

        setDefaultMethod()
    }

    private fun saveText() {
        var mTextConfigList:RealmList<TextConfigBean> = RealmList()

        for (redData:RedData in redList){
            val textConfig=TextConfigBean()
            textConfig.previous=redData.previous
            textConfig.next=redData.next
            mTextConfigList.add(textConfig)
        }


        textRealm.beginTransaction()
        val textItem=textRealm.createObject(ReciteTextBean::class.java)
        textItem.text=content
        textItem.textDate=Date(System.currentTimeMillis())
        textItem.textTitle=title
        textItem.textConfigList=mTextConfigList
        textRealm.commitTransaction()
    }

    private fun initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
         pvCustomOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, _, _, _ ->
            //返回的是选中位置

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
                        addNewReciteBook()
                    }
                }
                .isDialog(true)
                .build()

        pvCustomOptions.setPicker(textRealm.copyFromRealm(reciteBookResults))//添加数据
    }

    private fun addNewReciteBook() {
        showCustomDialog()
    }

    private fun showCustomDialog() {
        val builder=AlertDialog.Builder(this)
        builder.setTitle("创建记背本")

        val dialogContent=layoutInflater.inflate(R.layout.custom_dialog,null)
        builder.setView(dialogContent)

        val textInput=dialogContent.findViewById<TextInputEditText>(R.id.input_text)

        builder.setCancelable(false)


        builder.setPositiveButton("确定", { dialog, which ->
            run {
                resultText = textInput.text.toString()
                if (!resultText.trim().isEmpty()){
                    textRealm.beginTransaction()
                    val bookItem=textRealm.createObject(ReciteBookBean::class.java)
                    bookItem.bookTitle=resultText
                    bookItem.textNum=0
                    bookItem.picNum=0
                    bookItem.isTop=false
                    bookItem.bookDate= Date(System.currentTimeMillis())
                    textRealm.commitTransaction()
                }
                pvCustomOptions.setPicker(textRealm.copyFromRealm(reciteBookResults))
                dialog.dismiss()
            }
        })
        builder.setNegativeButton("取消", { dialog, which -> run {
            resultText = ""
            dialog.dismiss()
        } })
        val dialog=builder.create()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog.show()
    }

    private fun setDefaultMethod() {
        modifyText.isCursorVisible=false
        modifyText.highlightColor=Color.WHITE
        modifyText.setCanEdit(false)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0?.id) {
            R.id.write_button -> {
                if(p1){
                    modifyText.isCursorVisible=true
                    modifyText.setCanEdit(true)
                    modifyText.highlightColor=Color.WHITE
                }

                if(!p1){
                    modifyText.isCursorVisible=false
                    modifyText.setCanEdit(false)
                }
            }

            R.id.modify_button -> {
                if(p1){
                    modifyText.setCanModify(true)
                }

                if(!p1){
                    modifyText.setCanModify(false)
                }

            }

            R.id.see_button -> {
                if (p1) {
                    modifyText.setText(content)
                    modifyText.drawBlack()
                    modifyText.drawRed()
                }

                if (!p1) {
                    val curY = modifyText.scrollY
                    val curX = modifyText.scrollX
                    modifyText.changeText()
                    modifyText.drawRed()
                    modifyText.scrollTo(curX, curY)
                    redList=modifyText.redList
                }
            }


            R.id.switch_button -> {
                if(p1){
                    val changeList=modifyText.blackList
                    modifyText.blackList=modifyText.redList
                    modifyText.redList=changeList

                    modifyText.setText(content)
                    modifyText.drawBlack()
                    modifyText.drawRed()
                }

                if(!p1){
                    val changeList=modifyText.blackList
                    modifyText.blackList=modifyText.redList
                    modifyText.redList=changeList

                    modifyText.setText(content)
                    modifyText.drawBlack()
                    modifyText.drawRed()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textRealm.close()
    }
}
