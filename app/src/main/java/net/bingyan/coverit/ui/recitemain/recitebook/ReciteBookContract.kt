package net.bingyan.coverit.ui.recitemain.recitebook

import net.bingyan.coverit.BasePresenter
import net.bingyan.coverit.BaseView

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:38
 */
interface ReciteBookContract {
    interface View: BaseView<Presenter> {
        fun loadBookData()
    }

    interface  Presenter: BasePresenter {
        fun deleteBook()
        fun addBook()
        fun modifyBook()
    }
}