package net.bingyan.coverit.ui.recitemain

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import net.bingyan.coverit.R
import net.bingyan.coverit.adapter.logicadapter.ViewPagerAdapter
import net.bingyan.coverit.data.local.bean.ReciteBookBean
import net.bingyan.coverit.ui.recitemain.mine.MineFragment
import net.bingyan.coverit.ui.recitemain.mine.MinePresenter
import net.bingyan.coverit.ui.recitemain.recitebook.ReciteBookFragment
import net.bingyan.coverit.ui.recitemain.recitebook.ReciteBookPresenter
import net.bingyan.coverit.ui.recitemain.recitelist.ReciteListFragment
import net.bingyan.coverit.ui.recitemain.recitelist.ReciteListPresenter
import net.bingyan.coverit.ui.reciteother.ModifyPicActivity
import net.bingyan.coverit.util.BottomNavigationViewHelper
import net.bingyan.coverit.widget.TitleBar
import org.jetbrains.anko.intentFor
import java.io.File
import java.util.*

class ReciteMainActivity : AppCompatActivity() {
    private var menuItem: MenuItem? = null
    private lateinit var titleBar: TitleBar
    private lateinit var creatBookView: ImageView

    private lateinit var reciteBookResults: RealmResults<ReciteBookBean>

    private lateinit var resultText: String
    private lateinit var mainRealm: Realm

    private lateinit var reciteListFragment: ReciteListFragment
    private lateinit var reciteBookFragment: ReciteBookFragment
    private lateinit var mineFragment: MineFragment

    private var currentItem: Int = -1
    val TAKE_PHOTO = 1
    val CHOOSE_PHOTO = 2
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.recite_list -> {
                viewpager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.recite_book -> {
                viewpager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
        /*R.id.find -> {
            viewpager.currentItem=2
            return@OnNavigationItemSelectedListener true
        }*/
            R.id.mine -> {
                viewpager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        setupViewPager(viewpager = viewpager)
        if (currentItem != -1) viewpager.currentItem = currentItem
    }

    override fun onPause() {
        super.onPause()
        currentItem = viewpager.currentItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainRealm = Realm.getDefaultInstance()
        titleBar = findViewById(R.id.title_bar)
        creatBookView = findViewById(R.id.create_book)
        titleBar.setImmersive(true)
        titleBar.setBackgroundResource(R.drawable.bg_actionbar)

        BottomNavigationViewHelper.disableShiftMode(navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        creatBookView.setOnClickListener {
            showCustomDialog()
        }
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val titleList = listOf("记背列表", "记背本", "发现", "我的")
                titleBar.setTitle(titleList[position])
                titleBar.setTitleColor(ContextCompat.getColor(this@ReciteMainActivity, R.color.title_white))
                titleBar.setTitleSize(19f)
                if (position == 1) creatBookView.visibility = View.VISIBLE
                else creatBookView.visibility = View.GONE
                menuItem = navigation.menu.getItem(position)
                menuItem?.isChecked = true
            }

        })

    }


    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("创建记背本")

        val dialogContent = layoutInflater.inflate(R.layout.custom_dialog, null)
        builder.setView(dialogContent)

        val textInput = dialogContent.findViewById<TextInputEditText>(R.id.input_text)
        textInput.hint = "请输入记背本名称"

        builder.setCancelable(false)


        builder.setPositiveButton("确定", { dialog, which ->
            run {
                resultText = textInput.text.toString()
                if (!resultText.trim().isEmpty()) {
                    mainRealm.beginTransaction()
                    val bookItem = mainRealm.createObject(ReciteBookBean::class.java)
                    bookItem.bookTitle = resultText
                    bookItem.textNum = 0
                    bookItem.picNum = 0
                    bookItem.isTop = false
                    bookItem.bookDate = Date(System.currentTimeMillis())
                    mainRealm.commitTransaction()
                }
                reciteBookFragment.invalidateData()
                dialog.dismiss()
            }
        })
        builder.setNegativeButton("取消", { dialog, which ->
            run {
                resultText = ""
                dialog.dismiss()
            }
        })
        val dialog = builder.create()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                val outputImage = File(this.externalCacheDir, "output_image.jpg")
                if (outputImage.path != null)
                    startActivity(intentFor<ModifyPicActivity>("pic" to outputImage.path))
            }
            CHOOSE_PHOTO -> {
                var imagePath: String? = null
                if (data != null) {
                    val uri = data.data
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        assert(uri != null)
                        when {
                            "com.android.providers.media.documents" == uri!!.authority -> {
                                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                                val selection = MediaStore.Images.Media._ID + "=" + id
                                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                            }
                            "com.android.providers.downloads.documents" == uri.authority -> {
                                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public/public_downloads"), java.lang.Long.valueOf(docId)!!)
                                imagePath = getImagePath(contentUri, null)
                            }
                            "content".equals(uri.scheme, ignoreCase = true) -> imagePath = getImagePath(uri, null)
                            "file".equals(uri.scheme, ignoreCase = true) -> imagePath = uri.path
                        }
                    }
                }
                if (imagePath != null)
                    startActivity(intentFor<ModifyPicActivity>("pic" to imagePath))
            }

            else -> {
            }
        }
    }

    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    private fun setupViewPager(viewpager: ViewPager) {
        reciteListFragment = ReciteListFragment()
        reciteBookFragment = ReciteBookFragment()
        mineFragment = MineFragment()
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager).apply {
            addFragment(reciteListFragment)
            addFragment(reciteBookFragment)
            addFragment(mineFragment)
//            addFragment(discoverFragment)
        }
        viewpager.adapter = viewPagerAdapter

        ReciteListPresenter(reciteListFragment, this)
        ReciteBookPresenter(reciteBookFragment)
        MinePresenter(mineFragment)

    }


}
