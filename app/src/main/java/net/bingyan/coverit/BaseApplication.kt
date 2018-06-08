package net.bingyan.coverit

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.umeng.commonsdk.UMConfigure
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Author       zdlly
 * Date         2018.3.27
 * Time         23:19
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        Realm.init(this)
        /*
注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
*/
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,null)
        UMConfigure.setEncryptEnabled(true)
        val config = RealmConfiguration.Builder()
                .name("coverIt")
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(config)
        /*
注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）。
*/
    }

}