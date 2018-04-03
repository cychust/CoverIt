package net.bingyan.coverit.ui.reciteother

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_recite_book_detail.*
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.uiadapter.ReciteListAdapter
import net.bingyan.coverit.data.local.bean.ParentListBean
import net.bingyan.coverit.widget.SlideFromBottomPopup
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class ReciteBookDetailActivity : AppCompatActivity() {

    private lateinit var ivNewText: ImageView
    private lateinit var ivNewPic: ImageView

    private lateinit var rvList: RecyclerView
    private var titleList= mutableListOf<String>()
    private var timeList= mutableListOf<String>()
    private var picPathList= mutableListOf<String>()
    private var textList= mutableListOf<String>()

    private lateinit var bookTitle: String


    private var reciteBookDetailRealm= Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_book_detail)
        bookTitle=intent.getStringExtra("bookTitle")
        initView()
        loadListData()
    }

    private fun initView() {
        ivNewText=findViewById(R.id.iv_new_text)
        ivNewPic=findViewById(R.id.iv_new_pic)
        rvList=findViewById(R.id.rv_list)

        titleBar.setBackgroundResource(R.drawable.bg_actionbar)
        titleBar.setImmersive(true)

        titleBar.setTitle(bookTitle)
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
        val parentList:MutableList<ParentListBean> = mutableListOf()
        val parentListResults=reciteBookDetailRealm.where(ParentListBean::class.java).equalTo("belonging",bookTitle).findAll().sort("date", Sort.DESCENDING)
        for(parentItem in parentListResults){
            parentList.add(reciteBookDetailRealm.copyFromRealm(parentItem))
        }
        timeList.clear()
        titleList.clear()
        picPathList.clear()
        textList.clear()
        parentList.forEach {
            timeList.add(SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(it.date))
            titleList.add(it.title)
            picPathList.add(it.picpath)
            textList.add(it.text)
        }

        rvList.layoutManager= LinearLayoutManager(this)
        rvList.adapter= ReciteListAdapter(this,timeList,titleList,picPathList,textList)
    }
}
