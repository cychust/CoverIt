package net.bingyan.coverit.ui.recitemain.recitebook

import io.realm.Realm
import io.realm.RealmResults
import net.bingyan.coverit.BasePresenter
import net.bingyan.coverit.BaseView
import net.bingyan.coverit.data.local.bean.ReciteBookBean

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:38
 */
interface ReciteBookContract {
    interface View: BaseView<Presenter> {
        var bookRealm:Realm
        fun loadBookData(realmResults: RealmResults<ReciteBookBean>)
    }

    interface  Presenter: BasePresenter {
        fun deleteBook()
        fun addBook()
        fun modifyBook()
    }
}