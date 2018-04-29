package net.bingyan.coverit.ui.recitemain


import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
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
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView

    private var currentItem: Int = -1
    val TAKE_PHOTO = 1
    val CHOOSE_PHOTO = 2
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.recite_list -> {
                viewPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.recite_book -> {
                viewPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
        /*R.id.find -> {
            viewpager.currentItem=2
            return@OnNavigationItemSelectedListener true
        }*/
            R.id.mine -> {
                viewPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        if (currentItem != -1) viewPager.currentItem = currentItem
    }

    override fun onPause() {
        super.onPause()
        currentItem = viewPager.currentItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//A
        window.decorView.fitsSystemWindows=true

        mainRealm = Realm.getDefaultInstance()
        titleBar = findViewById(R.id.title_bar)
        creatBookView = findViewById(R.id.create_book)
        viewPager = findViewById(R.id.viewpager)
        bottomNavigationView = findViewById(R.id.navigation)

        titleBar.setImmersive(true)
        titleBar.setBackgroundResource(R.drawable.bg_actionbar)

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        creatBookView.setOnClickListener {
            showCustomDialog()
        }
        setupViewPager(viewpager = viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                menuItem = bottomNavigationView.menu.getItem(position)
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
        val textInputLayout = dialogContent.findViewById<TextInputLayout>(R.id.input_text_layout)
        textInputLayout.hint = "请输入记背本名称"

        builder.setCancelable(false)


        builder.setPositiveButton("确定", null)
        builder.setNegativeButton("取消", { dialog, which ->
            run {
                resultText = ""
                dialog.dismiss()
            }
        })
        val dialog = builder.create()
        //dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog.show()
        if(dialog.getButton(AlertDialog.BUTTON_POSITIVE)!=null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            resultText = textInput.text.toString()
            if (!resultText.trim().isEmpty()) {
                        if(mainRealm.where(ReciteBookBean::class.java).equalTo("bookTitle",resultText).findAll().isEmpty()){
                            mainRealm.beginTransaction()
                            val bookItem = mainRealm.createObject(ReciteBookBean::class.java)
                            bookItem.bookTitle = resultText
                            bookItem.textNum = 0
                            bookItem.picNum = 0
                            bookItem.isTop = false
                            bookItem.bookDate = Date(System.currentTimeMillis())
                            mainRealm.commitTransaction()
                            reciteBookFragment.invalidateData()
                            dialog.dismiss()
                        }else {
                            textInput.error="记背本已存在,不能重复创建!"
                            return@setOnClickListener
                        }
                    }else {
                        textInput.error = "记背本名称不能为空!"
                        return@setOnClickListener
                    }
        }
        }
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
                    val uri = geturi(data)
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        when {
                            "com.android.providers.media.documents" == uri.authority -> {
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
                    }else{
                        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
                        val cursor = this.contentResolver.query(uri, proj, null, null, null)
                        if (cursor != null)
                        {
                            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            cursor.moveToFirst()
                            imagePath = cursor.getString(column_index)// 图片的路径
                        }
                        cursor.close()
                    }
                }
                if (imagePath != null)
                    startActivity(intentFor<ModifyPicActivity>("pic" to imagePath))
            }

            else -> {
            }
        }
    }

    fun geturi(intent: android.content.Intent): Uri {
        var uri = intent.data
        val type = intent.type
        if (uri!!.scheme == "file" && type!!.contains("image/*")) {
            var path: String? = uri.encodedPath
            if (path != null) {
                path = Uri.decode(path)
                val cr = this.contentResolver
                val buff = StringBuffer()
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'$path'").append(")")
                val cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Images.ImageColumns._ID),
                        buff.toString(), null, null)
                var index = 0
                cur!!.moveToFirst()
                while (!cur.isAfterLast) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                    // set _id value
                    index = cur.getInt(index)
                    cur.moveToNext()
                }
                if (index == 0) {
                    // do nothing
                } else {
                    val uri_temp = Uri.parse("content://media/external/images/media/$index")
                    if (uri_temp != null) {
                        uri = uri_temp
                    }
                }
                cur.close()
            }
        }
        return uri
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
        ReciteBookPresenter(reciteBookFragment,this)
        MinePresenter(mineFragment)
    }

    fun refreshListData() {
        reciteListFragment.invalidateData()
    }
    fun refreshBookData(){
        reciteBookFragment.invalidateData()
    }
}
