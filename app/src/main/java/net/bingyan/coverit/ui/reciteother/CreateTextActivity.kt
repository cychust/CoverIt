package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_create_text.*
import net.bingyan.coverit.R
import net.bingyan.coverit.data.local.dataadapter.RedData
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick

class CreateTextActivity : AppCompatActivity() {

    private lateinit var btnSave:Button
    private lateinit var etTitle:EditText
    private lateinit var etContent:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_text)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initView()
    }

    private fun initView() {
        btnSave=findViewById(R.id.btn_save)
        etTitle=findViewById(R.id.et_title)
        etContent=findViewById(R.id.et_content)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle("创建新文本")
        titleBar.setTitleColor(ContextCompat.getColor(this,R.color.title_white))

        btnSave.onClick {
            val title=etTitle.text.toString().trim()
            val content=etContent.text.toString().trim()
            startActivity(intentFor<ModifyTextActivity>("title" to title,"content" to content,"redData" to mutableListOf<RedData>()))
            finish()
        }
    }
}
