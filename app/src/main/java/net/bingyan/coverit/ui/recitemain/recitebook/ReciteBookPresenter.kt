package net.bingyan.coverit.ui.recitemain.recitebook

/**
 * Author       zdlly
 * Date         2017.12.9
 * Time         0:38
 */
class ReciteBookPresenter(val reciteBookFragment: ReciteBookContract.View) :ReciteBookContract.Presenter{
    override fun start() {
        loadBookData()
    }
    init {
        reciteBookFragment.presenter=this
    }

    private fun loadBookData() {
        reciteBookFragment.loadBookData()
    }

    override fun deleteBook() {
        TODO("not implemented")
    }

    override fun addBook() {
        TODO("not implemented")
    }

    override fun modifyBook() {
        TODO("not implemented")
    }

}