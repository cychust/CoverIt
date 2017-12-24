package net.bingyan.coverit.ui.recitemain.recitelist

import android.app.Activity
import net.bingyan.coverit.widget.SlideFromBottomPopup

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:33
 */
class ReciteListPresenter(private val reciteListView:ReciteListContract.View,val parent:Activity):ReciteListContract.Presenter {
    override fun createNewText() {
        reciteListView.startTextActivity()
    }

    override fun createNewPic() {
        val popup=SlideFromBottomPopup(parent,parent)
        popup.showPopupWindow()
    }

    init {
       reciteListView.presenter=this
   }

    override fun start() {
       loadLists()
    }

    private fun loadLists() {
        reciteListView.loadListData()
    }

}