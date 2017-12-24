package net.bingyan.coverit.ui.recitemain.recitebook

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mancj.materialsearchbar.MaterialSearchBar
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.uiadapter.ReciteBookAdapter

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:37
 */
class ReciteBookFragment : Fragment(),ReciteBookContract.View, MaterialSearchBar.OnSearchActionListener {
    override lateinit var presenter: ReciteBookContract.Presenter

    private lateinit var searchBar:MaterialSearchBar

    private lateinit var  lastSearches:List<String>
    private lateinit var rvBookList:RecyclerView
    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root=inflater.inflate(R.layout.fragment_recitebook,container,false)
        with(root){
            searchBar=findViewById(R.id.searchBar)
            rvBookList=findViewById(R.id.rv_book)
        }
        searchBar.setOnSearchActionListener(this)
        searchBar.setPlaceHolder("搜索你想要的记背本...")
        loadBookData()
        return root
    }
    override fun loadBookData() {
        lastSearches= listOf("111","222","333","444","555","666","777","888","999")
        rvBookList.layoutManager= GridLayoutManager(context,2)
        rvBookList.adapter= ReciteBookAdapter(context,lastSearches,lastSearches,lastSearches,lastSearches)
    }

    override fun onButtonClicked(buttonCode: Int) {

    }

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {

    }
}
