package net.bingyan.coverit.ui.recitemain.recitelist

import net.bingyan.coverit.BasePresenter
import net.bingyan.coverit.BaseView

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:31
 */
interface ReciteListContract {
    interface View:BaseView<Presenter>{
        fun loadListData()
        fun startTextActivity()
    }

    interface  Presenter:BasePresenter{
        fun createNewText()
        fun createNewPic()
    }
}