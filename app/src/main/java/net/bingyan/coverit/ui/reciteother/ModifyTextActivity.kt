package net.bingyan.coverit.ui.reciteother

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_modify_pic.*
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.bean.RedData
import net.bingyan.coverit.widget.ModifyTextView
import org.jetbrains.anko.backgroundColor
import java.util.*

class ModifyTextActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var btnSave: Button

    private lateinit var modifyText: ModifyTextView
    private lateinit var cbSwitch: CheckBox
    private lateinit var cbSee: CheckBox
    private lateinit var cbWrite: CheckBox
    private lateinit var cbModify: CheckBox
    private lateinit var title: String

    private lateinit var content: String

    private var redList = ArrayList<RedData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_text)
        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")

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

        setDefaultMethod()
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
                    modifyText.drawRed()
                }

                if (!p1) {
                    val curY = modifyText.scrollY
                    val curX = modifyText.scrollX
                    modifyText.changeText()
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
}
