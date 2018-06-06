package net.bingyan.coverit.ui.recitemain.recitebook

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mancj.materialsearchbar.MaterialSearchBar
import io.realm.Realm
import io.realm.RealmResults
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.uiadapter.ReciteBookAdapter
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.ui.recitemain.ReciteMainActivity
import org.jetbrains.anko.backgroundResource
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:37
 */
class ReciteBookFragment : Fragment(),ReciteBookContract.View, MaterialSearchBar.OnSearchActionListener {


    override lateinit var reciteBookRealm: Realm

    override lateinit var presenter: ReciteBookContract.Presenter

    private lateinit var searchBar:MaterialSearchBar

    private lateinit var  lastSearches:List<String>
    private lateinit var rvBookList:RecyclerView


    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        reciteBookRealm.close()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root=inflater.inflate(R.layout.fragment_recitebook,container,false)
        with(root){
            searchBar=findViewById(R.id.searchBar)
            rvBookList=findViewById(R.id.rv_book)
        }
        searchBar.setOnSearchActionListener(this)
        searchBar.setPlaceHolder("搜索你想要的记背本...")
        searchBar.visibility=View.GONE//todo
        return root
    }
    override fun loadBookData(realmResults: RealmResults<ReciteBookBean>) {
        val titleList:MutableList<String> = mutableListOf()
        val textNumList:MutableList<String> = mutableListOf()
        val picNumList:MutableList<String> = mutableListOf()
        val dateList:MutableList<String> = mutableListOf()
        reciteBookRealm= Realm.getDefaultInstance()
        for (realmResult:ReciteBookBean in realmResults){
            if(reciteBookRealm.copyFromRealm(realmResult).isTop){
                titleList.add(0,(reciteBookRealm.copyFromRealm(realmResult)).bookTitle.toString())
                textNumList.add(0,(reciteBookRealm.copyFromRealm(realmResult)).textNum.toString())
                picNumList.add(0,(reciteBookRealm.copyFromRealm(realmResult)).picNum.toString())
                dateList.add(0,SimpleDateFormat("yy.MM.dd", Locale.CHINA).format((reciteBookRealm.copyFromRealm(realmResult)).bookDate))
            }else{
                titleList.add((reciteBookRealm.copyFromRealm(realmResult)).bookTitle.toString())
                textNumList.add((reciteBookRealm.copyFromRealm(realmResult)).textNum.toString())
                picNumList.add((reciteBookRealm.copyFromRealm(realmResult)).picNum.toString())
                dateList.add(SimpleDateFormat("yy.MM.dd", Locale.CHINA).format((reciteBookRealm.copyFromRealm(realmResult)).bookDate))
            }
        }
        rvBookList.layoutManager= GridLayoutManager(context,2) as RecyclerView.LayoutManager?
        rvBookList.adapter= ReciteBookAdapter(context, (activity  as ReciteMainActivity?)!!,titleList ,textNumList,picNumList,dateList)
        rvBookList.itemAnimator=DefaultItemAnimator()
      // (rvBookList.adapter as ReciteBookAdapter).refreshSlef()
        if (rvBookList.adapter.itemCount==0){
            rvBookList.background= ContextCompat.getDrawable(context!!,R.drawable.nothing)
        }else rvBookList.backgroundResource=R.color.white
    }

    override fun onButtonClicked(buttonCode: Int) {

    }

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {

    }

    fun invalidateData(){
        presenter.start()
        rvBookList.adapter.notifyDataSetChanged()
    }

}
