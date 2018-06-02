package net.bingyan.coverit.ui.recitemain.mine

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:41
 */
class MinePresenter(private val mineFragment: MineContract.View):MineContract.Presenter {
    init {
        mineFragment.presenter=this
    }
    override fun start() {

    }

    override fun onClickFeedback() {

    }

    override fun onClickScore() {

    }

    override fun onClickAbout() {

    }
}