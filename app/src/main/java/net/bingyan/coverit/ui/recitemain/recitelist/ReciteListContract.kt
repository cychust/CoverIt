package net.bingyan.coverit.ui.recitemain.recitelist

import io.realm.Realm
import net.bingyan.coverit.BasePresenter
import net.bingyan.coverit.BaseView
import net.bingyan.coverit.data.local.bean.ParentListBean

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:31
 */
interface ReciteListContract {
    interface View:BaseView<Presenter>{
        var reciteListRealm:Realm
        fun loadListData(parentList: MutableList<ParentListBean>)
        fun startTextActivity()
    }

    interface  Presenter:BasePresenter{
        fun createNewText()
        fun createNewPic()
    }
}