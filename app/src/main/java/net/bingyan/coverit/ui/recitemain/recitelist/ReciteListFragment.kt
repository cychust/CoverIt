package net.bingyan.coverit.ui.recitemain.recitelist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import io.realm.Realm
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.uiadapter.ReciteListAdapter
import net.bingyan.coverit.data.local.bean.ParentListBean
import net.bingyan.coverit.data.local.dataadapter.RedData
import net.bingyan.coverit.ui.recitemain.ReciteMainActivity
import net.bingyan.coverit.ui.reciteother.ModifyTextActivity
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.intentFor
import java.util.*

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:30
 */
class ReciteListFragment: Fragment(),ReciteListContract.View {
    override lateinit var reciteListRealm: Realm

    private val TAKE_PHOTO=1
    private val SELECT_ALBUM=2

    private lateinit var ivNewText:ImageView
    private lateinit var ivNewPic:ImageView
    private lateinit var rvList:RecyclerView

    private lateinit var llList:LinearLayout

    private var titleList= mutableListOf<String>()

    private var timeList= mutableListOf<Date>()
    private var picPathList= mutableListOf<String>()
    private var textList= mutableListOf<String>()
    override lateinit var presenter: ReciteListContract.Presenter



    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        reciteListRealm.close()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val root=inflater.inflate(R.layout.fragment_recitelist,container,false)
        with(root){
            ivNewText=findViewById(R.id.iv_new_text)
            ivNewPic=findViewById(R.id.iv_new_pic)
            rvList=findViewById(R.id.rv_list)
            llList=findViewById(R.id.ll_list)
        }
        ivNewText.setOnClickListener {
            presenter.createNewText()
        }
        ivNewPic.setOnClickListener {
            presenter.createNewPic()
        }
        return root
    }

    override fun startTextActivity() {
        startActivity(context!!.intentFor<ModifyTextActivity>("title" to "", "content" to "", "redData" to mutableListOf<RedData>()))
    }
    override fun loadListData(parentList: MutableList<ParentListBean>) {
        timeList.clear()
        titleList.clear()
        picPathList.clear()
        textList.clear()
        parentList.forEach {
            if (it.isTop) {
                timeList.add(0, it.date)
                titleList.add(0, it.title)
                picPathList.add(0, it.picpath)
                textList.add(0, it.text)
            } else {
                timeList.add(it.date)
                titleList.add(it.title)
                picPathList.add(it.picpath)
                textList.add(it.text)
            }
        }
        rvList.layoutManager= LinearLayoutManager(context)
        rvList.adapter= ReciteListAdapter(context!!, (activity as ReciteMainActivity?)!!,timeList,titleList,picPathList,textList)
        if (rvList.adapter.itemCount==0){
            llList.backgroundResource=R.drawable.nothing_bg
        }else llList.backgroundResource=R.color.white
    }

    fun invalidateData() {
        presenter.start()
        rvList.adapter.notifyDataSetChanged()
    }

}
