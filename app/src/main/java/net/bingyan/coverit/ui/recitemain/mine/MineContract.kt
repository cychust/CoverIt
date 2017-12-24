package net.bingyan.coverit.ui.recitemain.mine

import net.bingyan.coverit.BasePresenter
import net.bingyan.coverit.BaseView

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:41
 */
interface MineContract {
    interface View: BaseView<Presenter> {

    }

    interface  Presenter: BasePresenter {
        fun onClickFeedback()
        fun onClickScore()
        fun onClickAbout()
    }
}