package com.devy.global.usecase

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by JeYHoon on 2025. 10. 7..
 */
class AppInfoUseCase @Inject constructor() {

    private fun getPackageInfo(context: Context): PackageInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.applicationContext.packageName, PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                context.packageManager.getPackageInfo(
                    context.applicationContext.packageName, 0
                )
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }
    }

    fun getAppVersionName(context: Context): String {
        var versionName = ""

        try {
            versionName = getPackageInfo(context)?.versionName ?: ""
        } catch (ex: Exception) {
            Timber.e(ex)
        }

        return versionName
    }

    fun getAppVersionCode(context: Context): String {
        var versionCode = ""

        try {
            versionCode = getPackageInfo(context)?.versionCode.toString()
        } catch (ex: Exception) {
            Timber.e(ex)
        }

        return versionCode
    }
}