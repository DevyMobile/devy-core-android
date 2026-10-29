package com.devy.global.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devy.global.DLog.log
import kotlin.math.abs

/**
 * Created by JeYHoon on 2025. 10. 3..
 */
object NotificationHelper {

    fun showChatNotification(
        context: Context,
        channelType: ChannelType,
        intent: Intent,
        notificationId: Int,
        requestCode: Int,
        title: String,
        content: String,
        iconResId: Int,
        groupKey: String = "",
    ) {
        // Android 8.0 이상에서는 알림 채널이 필수
        createNotificationChannel(context, channelType)

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelType.id)
            .setSmallIcon(iconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setGroup(groupKey)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .setAutoCancel(true) // 탭하면 자동으로 사라지게

        val summaryBuilder = NotificationCompat.Builder(context, channelType.id)
            .setSmallIcon(iconResId)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setBigContentTitle("새 알림")
                    .setSummaryText("새로운 알림")
            )
            .setContentTitle("새 알림")
            .setContentText("읽지 않은 알림들이 있습니다.")
            .setContentIntent(pendingIntent)
            .setGroup(groupKey)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .setGroupSummary(true)

        with(NotificationManagerCompat.from(context)) {
            // Android 13 (API 33) 이상에서는 알림 권한이 필요
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                "Permission not granted for notifications.".log()
                // 권한이 없다면 알림을 보낼 수 없음.
                // 실제 앱에서는 이전에 사용자에게 권한을 요청하는 로직이 필요합니다.
                return
            }

            val dynamicSummaryId = getNegativeHashCode(groupKey)

            notify(notificationId, builder.build()) // 알림 표시
            notify(dynamicSummaryId, summaryBuilder.build())
        }
    }

    fun dismissNotification(context: Context, notificationId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }

    fun createNotification(
        context: Context,
        channelType: ChannelType,
        title: String,
        content: String,
        iconResId: Int,
        groupKey: String = "",
    ): Notification {
        createNotificationChannel(context, channelType)
        return NotificationCompat.Builder(context, channelType.id)
            .setSmallIcon(iconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setGroup(groupKey)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel(
        context: Context,
        channelType: ChannelType,
    ) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(channelType.id, channelType.channelName, importance).apply {
                description = channelType.description
            }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getNegativeHashCode(key: String): Int {
        val hash = key.hashCode()
        // 0일 경우를 대비해 -1을 기본 분기로 두고, 양수일 경우 음수로 변환합니다.
        return if (hash == 0) SUMMARY_ID else -abs(hash)
    }

    private const val SUMMARY_ID = -1

    data class ChannelType(
        val id: String,
        val channelName: String,
        val description: String,
    )
}
