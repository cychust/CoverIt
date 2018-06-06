package net.bingyan.coverit.ui.recitemain.recitelist

import android.app.Activity
import com.blankj.utilcode.util.FileUtils
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import net.bingyan.coverit.data.local.bean.*
import net.bingyan.coverit.widget.SlideFromBottomPopup
import java.io.File

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:33
 */
class ReciteListPresenter(private val reciteListView: ReciteListContract.View, val parent: Activity) : ReciteListContract.Presenter {
    private var reciteListRealm = Realm.getDefaultInstance()
    override fun createNewText() {
        reciteListView.startTextActivity()
    }

    override fun createNewPic() {
        val popup = SlideFromBottomPopup(parent, parent)
        popup.showPopupWindow()
    }

    init {
        reciteListView.presenter = this
        reciteListView.reciteListRealm = this.reciteListRealm
    }

    override fun start() {
        loadLists()
    }

    private fun loadLists() {
        val textResultList = reciteListRealm.where(ReciteTextBean::class.java).findAll().sort("textDate", Sort.DESCENDING)
        val picResultList = reciteListRealm.where(RecitePicBean::class.java).findAll().sort("picDate", Sort.DESCENDING)
        val parentList = mergeList(textResultList, picResultList)
        reciteListView.loadListData(parentList)
    }

    private fun mergeList(textResultList: RealmResults<ReciteTextBean>, picResultList: RealmResults<RecitePicBean>): MutableList<ParentListBean> {
        val parentList: MutableList<ParentListBean> = mutableListOf()
        val oldResults = reciteListRealm.where(ParentListBean::class.java).findAll()
        reciteListRealm.executeTransaction {
            oldResults.deleteAllFromRealm()
        }

        for (textResult in textResultList) {
            reciteListRealm.beginTransaction()
            val parentItem = reciteListRealm.createObject(ParentListBean::class.java)
            parentItem.title = textResult.textTitle
            parentItem.date = textResult.textDate
            parentItem.isTop = textResult.isTop
            parentItem.text = textResult.text
            parentItem.picpath = ""
            parentItem.belonging = textResult.belonging
            reciteListRealm.commitTransaction()
        }
        for (picResult in picResultList) {
            if (FileUtils.isFileExists(picResult.picPath)) {
                reciteListRealm.beginTransaction()
                val parentItem = reciteListRealm.createObject(ParentListBean::class.java)
                parentItem.title = picResult.belonging//todo
                parentItem.date = picResult.picDate
                parentItem.isTop = picResult.isTop
                parentItem.text = ""
                parentItem.picpath = picResult.picPath
                parentItem.belonging = picResult.belonging
                reciteListRealm.commitTransaction()
            } else {
                reciteListRealm.beginTransaction()
                val item = reciteListRealm.where(RecitePicBean::class.java).equalTo("picPath", picResult.picPath).findFirst()


                val book = reciteListRealm.where(ReciteBookBean::class.java).equalTo("bookTitle", picResult.belonging).findFirst()
                if (book != null)
                    book.picNum--
                item?.deleteFromRealm()
                reciteListRealm.commitTransaction()
            }
        }

        val parentListResults = reciteListRealm.where(ParentListBean::class.java).findAll().sort("date", Sort.DESCENDING)
        for (parentItem in parentListResults) {
            parentList.add(reciteListRealm.copyFromRealm(parentItem))
        }

        return parentList
    }

}