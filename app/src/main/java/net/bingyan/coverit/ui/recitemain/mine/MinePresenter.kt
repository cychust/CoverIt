package net.bingyan.coverit.ui.recitemain.mine

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:41
 */
class MinePresenter(val mineFragment: MineContract.View):MineContract.Presenter {
    init {
        mineFragment.presenter=this
    }
    override fun start() {

    }

    override fun onClickFeedback() {
        TODO("not implemented")
    }

    override fun onClickScore() {
        TODO("not implemented")
    }

    override fun onClickAbout() {
        TODO("not implemented")
    }
}