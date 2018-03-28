package net.bingyan.coverit

import android.app.Application
import com.blankj.utilcode.util.Utils
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
        val config = RealmConfiguration.Builder()
                .name("coverIt")
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(config)
    }

}