package net.bingyan.coverit.ui.reciteother

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

        modifyText.setText(content)
        modifyText.isCursorVisible = false
        modifyText.background = ContextCompat.getDrawable(this, R.drawable.grain)
        modifyText.movementMethod=ScrollingMovementMethod()
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0?.id) {


            R.id.write_button -> {
                if(p1){

                }

                if(!p1){

                }
            }

            R.id.modify_button -> {
                if(p1){

                }

                if(!p1){

                }

            }

            R.id.see_button -> {
                if (p1) {
                    val curY = modifyText.scrollY
                    val curX = modifyText.scrollX
                    modifyText.changeText()
                    modifyText.scrollTo(curX, curY)
                    redList=modifyText.redList
                }

                if (!p1) {
                    modifyText.setText(content)
                    modifyText.drawRed()
                }
            }


            R.id.switch_button -> {
                if(p1){

                }

                if(!p1){

                }
            }

        }
    }
}
