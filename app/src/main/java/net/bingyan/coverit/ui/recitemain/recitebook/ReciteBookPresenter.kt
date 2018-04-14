package net.bingyan.coverit.ui.recitemain.recitebook

import android.app.Activity
import io.realm.Realm
import io.realm.Sort
import net.bingyan.coverit.data.local.bean.ReciteBookBean

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:38
 */
class ReciteBookPresenter(private val reciteBookFragment: ReciteBookContract.View, parent: Activity) :ReciteBookContract.Presenter{
    private var reciteBookRealm:Realm = Realm.getDefaultInstance()
    override fun start() {
        loadBookData()
    }
    init {
        reciteBookFragment.presenter=this
        reciteBookFragment.reciteBookRealm=reciteBookRealm
    }

    private fun loadBookData() {
        val bookList=reciteBookRealm.where(ReciteBookBean::class.java).findAll().sort("bookDate",Sort.DESCENDING)
        reciteBookFragment.loadBookData(bookList)
    }
}