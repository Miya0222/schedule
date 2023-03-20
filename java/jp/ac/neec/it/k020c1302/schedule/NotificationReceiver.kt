package jp.ac.neec.it.k020c1302.schedule

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    //通知チャンネルID
    private val CHANNEL_ID = "schedule_notification_channel"

    override fun onReceive(context: Context, intent: Intent) {
        // 通知を作成する
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = "時間割アプリ"
        val message = "指定した時間です"
        val icon = android.R.drawable.ic_dialog_info
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(icon)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(0, notification)
    }
}