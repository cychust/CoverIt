package net.bingyan.coverit.ui.reciteother

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_modify_pic.*
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.data.local.bean.ReciteTextBean
import net.bingyan.coverit.data.local.bean.TextConfigBean
import net.bingyan.coverit.data.local.dataadapter.RedData
import net.bingyan.coverit.util.FileUtils
import net.bingyan.coverit.widget.ModifyTextView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


class ModifyTextActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var btnSave: Button

    private lateinit var modifyText: ModifyTextView
    private lateinit var modifyTitle: EditText
    private lateinit var cbSwitch: CheckBox
    private lateinit var cbSee: CheckBox
    private lateinit var cbWrite: CheckBox
    private lateinit var cbModify: CheckBox
    private lateinit var title: String

    private lateinit var content: String

    private lateinit var pvCustomOptions: OptionsPickerView<ReciteBookBean>

    private var redList = mutableListOf<RedData>()

    private lateinit var textRealm: Realm
    private lateinit var reciteBookResults: RealmResults<ReciteBookBean>

    private lateinit var resultText: String

    private lateinit var textItem: ReciteTextBean

    private lateinit var lcText: ConstraintLayout
    private lateinit var textGuide: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_text)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//A
        window.decorView.fitsSystemWindows = true

        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")

        textRealm = Realm.getDefaultInstance()
        initView()
    }

    private fun initView() {
        btnSave = findViewById(R.id.btn_save)
        modifyText = findViewById(R.id.modify_text)
        modifyTitle = findViewById(R.id.modify_title)
        cbSwitch = findViewById(R.id.switch_button)
        cbSee = findViewById(R.id.see_button)
        cbWrite = findViewById(R.id.write_button)
        cbModify = findViewById(R.id.modify_button)
        lcText = findViewById(R.id.cl_text)
        textGuide = findViewById(R.id.text_guide)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle("文本记背")
        titleBar.setTitleColor(ContextCompat.getColor(this, R.color.title_white))

        cbSwitch.setOnCheckedChangeListener(this)
        cbSee.setOnCheckedChangeListener(this)
        cbWrite.setOnCheckedChangeListener(this)
        cbModify.setOnCheckedChangeListener(this)

        cbWrite.isChecked = true
        cbSee.isChecked = true
        cbModify.isChecked = true
        cbSwitch.isChecked = false

        modifyTitle.setText(title)
        modifyText.setText(content)

        modifyText.backgroundColor = Color.WHITE
        modifyText.isCursorVisible = false
        modifyText.isLongClickable = false

        modifyText.drawBlack()

        val redDataList = intent.getSerializableExtra("redData") as MutableList<TextConfigBean>

        redDataList.forEach {
            redList.add(RedData(it.previous, it.next))
        }

        modifyText.redList = redList as ArrayList<RedData>
        modifyText.drawRed()
        btnSave.onClick {
            when {
                modifyTitle.text.isEmpty() -> Toast.makeText(this@ModifyTextActivity, "标题不能为空!", Toast.LENGTH_SHORT).show()
                modifyText.text.isEmpty() -> Toast.makeText(this@ModifyTextActivity, "内容不能为空!", Toast.LENGTH_SHORT).show()
                else -> {
                    title = modifyTitle.text.toString()
                    content = modifyText.text.toString()
                    reciteBookResults = textRealm.where(ReciteBookBean::class.java)
                            .findAll()
                    initCustomOptionPicker()
                    pvCustomOptions.show()
                }
            }
        }


        if (FileUtils.isTextFirstOpen(this)) {
            textGuide.visibility = View.VISIBLE
            textGuide.onClick {
                textGuide.visibility = View.GONE
            }
        }
        setDefaultMethod()
    }

    private fun saveText() {
        modifyText.calculateText()
        redList = modifyText.redList

        textRealm.beginTransaction()
        textItem = textRealm.createObject(ReciteTextBean::class.java)
        textItem.text = content.trim()
        textItem.textDate = Date(System.currentTimeMillis())
        textItem.textTitle = modifyTitle.text.trim().toString()
        for (redData: RedData in redList) {
            val textConfig = TextConfigBean()
            textConfig.previous = redData.previous
            textConfig.next = redData.next
            textItem.textConfigList.add(textConfig)
        }
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
            saveText()
            val selectedItem = reciteBookResults[options1]
            textRealm.executeTransaction {
                textItem.belonging = selectedItem!!.pickerViewText
            }
            textRealm.executeTransaction({
                //先查找后得到对象
                val user = textRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", selectedItem!!.pickerViewText).findFirst()
                user!!.textNum += 1
                user!!.textList.add(textItem)
                user.bookDate = Date(System.currentTimeMillis())
                Toast.makeText(this, "已成功添加", Toast.LENGTH_SHORT).show()
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

        pvCustomOptions.setPicker(textRealm.copyFromRealm(reciteBookResults))//添加数据
    }

    private fun addNewReciteBook() {
        showCustomDialog()
    }

    private fun showCustomDialog() {
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
        dialog.show()
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                resultText = textInput.text.toString()
                if (!resultText.trim().isEmpty()) {
                    if (textRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", resultText).findAll().isEmpty()) {
                        textRealm.beginTransaction()
                        val bookItem = textRealm.createObject(ReciteBookBean::class.java)
                        bookItem.bookTitle = resultText
                        bookItem.textNum = 0
                        bookItem.picNum = 0
                        bookItem.isTop = false
                        bookItem.bookDate = Date(System.currentTimeMillis())
                        textRealm.commitTransaction()
                    } else {
                        textInput.error = "记背本已存在,不能重复创建!"
                        return@setOnClickListener
                    }
                } else {
                    textInput.error = "记背本名称不能为空!"
                    return@setOnClickListener
                }
                pvCustomOptions.setPicker(textRealm.copyFromRealm(reciteBookResults))
                dialog.dismiss()
                pvCustomOptions.setSelectOptions(reciteBookResults.size - 1)
                pvCustomOptions.show()
            }
        }
    }

    private fun setDefaultMethod() {
        modifyText.isCursorVisible = true
        modifyText.highlightColor = ContextCompat.getColor(this, R.color.colorPrimary)
        modifyText.setCanEdit(true)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0?.id) {
            R.id.write_button -> {
                if (p1) {
                    modifyText.isCursorVisible = true
                    modifyText.setCanEdit(true)
                    modifyText.highlightColor = ContextCompat.getColor(this, R.color.colorPrimary)
                }

                if (!p1) {
                    modifyText.highlightColor = Color.WHITE
                    content = modifyText.text.toString()
                    modifyText.isCursorVisible = false
                    modifyText.setCanEdit(false)
                }
            }

            R.id.modify_button -> {
                if (p1) {
                    modifyText.setCanModify(true)
                }

                if (!p1) {
                    modifyText.setCanModify(false)
                }

            }

            R.id.see_button -> {
                if (p1) {
                    cbWrite.isEnabled = true
                    modifyText.setText(content)
                    modifyText.drawBlack()
                    modifyText.drawRed()
                }

                if (!p1) {
                    cbWrite.isEnabled = false
                    val curY = modifyText.scrollY
                    val curX = modifyText.scrollX
                    modifyText.changeText()
                    modifyText.drawRed()
                    modifyText.scrollTo(curX, curY)
                    redList = modifyText.redList
                }
            }


            R.id.switch_button -> {
                if (modifyText.text.isEmpty()) {

                } else {
                    if (p1) {
                        val changeList = modifyText.blackList
                        modifyText.blackList = modifyText.redList
                        modifyText.redList = changeList

                        modifyText.setText(content)
                        modifyText.drawBlack()
                        modifyText.drawRed()
                    }
                    if (!p1) {
                        val changeList = modifyText.blackList
                        modifyText.blackList = modifyText.redList
                        modifyText.redList = changeList

                        modifyText.setText(content)
                        modifyText.drawBlack()
                        modifyText.drawRed()
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textRealm.close()
    }
}
