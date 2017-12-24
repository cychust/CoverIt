package net.bingyan.coverit.ui.recitemain.recitelist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.uiadapter.ReciteListAdapter
import net.bingyan.coverit.ui.reciteother.CreateTextActivity
import org.jetbrains.anko.support.v4.intentFor

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:30
 */
class ReciteListFragment: Fragment(),ReciteListContract.View {
    private val TAKE_PHOTO=1

    private val SELECT_ALBUM=2
    private lateinit var ivNewText:ImageView
    private lateinit var ivNewPic:ImageView

    private lateinit var rvList:RecyclerView

    override lateinit var presenter: ReciteListContract.Presenter
    override fun onResume() {
        super.onResume()
        presenter.start()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val root=inflater.inflate(R.layout.fragment_recitelist,container,false)
        with(root){
            ivNewText=findViewById(R.id.iv_new_text)
            ivNewPic=findViewById(R.id.iv_new_pic)
            rvList=findViewById(R.id.rv_list)
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
        startActivity(intentFor<CreateTextActivity>())
    }

    override fun loadListData() {
        val timeList= listOf("2017-10-01","2017-10-02","2017-10-03","2017-10-04","2017-10-05")
        val nameList= listOf("马克思主义原理","马克思主义原理","马克思主义原理","马克思主义原理","马克思主义原理")
        rvList.layoutManager= LinearLayoutManager(context)
        rvList.adapter= ReciteListAdapter(this.context!!,timeList,nameList,nameList)
    }

}
