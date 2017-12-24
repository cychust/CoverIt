package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_recite_book_detail.*
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.uiadapter.ReciteListAdapter
import net.bingyan.coverit.widget.SlideFromBottomPopup
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick

class ReciteBookDetailActivity : AppCompatActivity() {

    private lateinit var ivNewText: ImageView
    private lateinit var ivNewPic: ImageView

    private lateinit var rvList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_book_detail)
        initView()
        loadListData()
    }

    private fun initView() {
        ivNewText=findViewById(R.id.iv_new_text)
        ivNewPic=findViewById(R.id.iv_new_pic)
        rvList=findViewById(R.id.rv_list)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle("马克思主义原理")
        titleBar.setTitleColor(ContextCompat.getColor(this,R.color.title_white))
        ivNewText.onClick {
            startActivity(intentFor<CreateTextActivity>())
        }

        ivNewPic.onClick {
            val popup= SlideFromBottomPopup(this@ReciteBookDetailActivity,this@ReciteBookDetailActivity)
            popup.showPopupWindow()
        }
    }

    private fun loadListData() {
        val timeList= listOf("2017-10-01","2017-10-02","2017-10-03","2017-10-04","2017-10-05")
        val nameList= listOf("马克思主义原理","马克思主义原理","马克思主义原理","马克思主义原理","马克思主义原理")
        rvList.layoutManager= LinearLayoutManager(this)
        rvList.adapter= ReciteListAdapter(this,timeList,nameList,nameList)
    }
}
