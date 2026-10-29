package com.devy.global.usecase

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by JeYHoon on 2025. 10. 3..
 */
class AccessResourceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun string(@StringRes resId: Int): String = context.getString(resId)
    fun string(@StringRes resId: Int, vararg formatArgs: Any): String = context.getString(resId, *formatArgs)
    fun stringArray(@ArrayRes resId: Int): Array<String> = context.resources.getStringArray(resId)
    fun pluralString(@PluralsRes resId: Int, count: Int, vararg formatArgs: Any): String = context.resources.getQuantityString(resId, count, *formatArgs)
}