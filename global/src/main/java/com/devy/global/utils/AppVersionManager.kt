package com.devy.global.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.devy.global.DLog.log

/**
 * Created by JeYHoon on 2025. 10. 3..
 */
object AppVersionManager {

    var versionName: String = ""
        private set
    var versionCode: Int = 0
        private set

    fun init(context: Context) {
        try {
            val packageName = context.packageName
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(packageName, 0)
            versionName = packageInfo.versionName ?: ""
            versionCode = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.log()
        }
    }
}