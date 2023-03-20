package jp.ac.neec.it.k020c1302.schedule

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import java.util.Calendar

class NotificationService : Service() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    //通知チャンネルID
    private val CHANNEL_ID = "schedule_notification_channel"

    //サービスはUIを持たないため、コンテキストを定義しなければDBヘルパーを使うことができないため定義する
    private lateinit var _helper: DatabaseHelper
    private lateinit var context: Context

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        //DBヘルパーの生成
        context = this
        _helper = DatabaseHelper(context)

        //AlarmManagerを取得
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //PendingIntentを作成
        val intent = Intent(this, NotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)


        // 通知チャンネルの作成
        val channel = NotificationChannel(CHANNEL_ID, "チャンネル名", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //時と分と通知するかどうかのSELECT文
        val sqlSelect = "SELECT * FROM time"
        //DBオブジェクトを取得
        val db = _helper.readableDatabase
        //SQL実行
        val cursor = db.rawQuery(sqlSelect, null)
        //現在時刻を取得
        val now = Calendar.getInstance()
        //次に来る時間を初期化
        var nextAlarmTime:Calendar? = null

        //DBを一つずつ比較していく
        while (cursor.moveToNext()){
            //カラムのインデックスを取得
            val indexHour = cursor.getColumnIndex("hour")
            val indexMinute = cursor.getColumnIndex("minute")
            val indexNotify = cursor.getColumnIndex("notify")
            //インデックスからデータを取得
            val hour = cursor.getInt(indexHour)
            val minute = cursor.getInt(indexMinute)
            val notify = cursor.getInt(indexNotify)

            //取得した時間を設定
            val time = Calendar.getInstance()
            time.set(Calendar.HOUR_OF_DAY, hour)
            time.set(Calendar.MINUTE, minute)
            time.set(Calendar.SECOND, 0)

            //取得した時間と現在を比べた時に、現在よりも前の時間だった場合、通知する時間を1日進める　
            if (time.before(now)) {
                time.add(Calendar.DAY_OF_MONTH, 1)
            }

            //現在時刻と比較
            if(time.after(now)){
                //次に来る時間がまだ見つかっていないか、より早い時間が見つかった場合
                if(nextAlarmTime == null || time.before(nextAlarmTime)){
                    //次に来る時間を更新する
                    if(notify == 1) {
                        nextAlarmTime = time
                    }
                }
            }
        }
        cursor.close()

        //AlarmManagerに指定した時間にPendingIntentを送信するように設定
        if(nextAlarmTime != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTime.timeInMillis, pendingIntent)
        }

        // 指定された時間間隔でサービスを呼び出すためのAlarmManagerを作成する
        val interval = 30 * 1000 // 1分ごとに処理を行う場合
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getService(this, 0, Intent(this, NotificationService::class.java), PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval.toLong(), pendingIntent)

        return START_STICKY
    }
}