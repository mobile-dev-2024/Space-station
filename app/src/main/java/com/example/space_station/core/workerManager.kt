package com.example.space_station.core

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        // Data 객체로부터 title과 content를 가져옵니다
        val title = inputData.getString("title") ?: "알림"
        val content = inputData.getString("content") ?: "내용이 없습니다."

        // 알림을 표시하는 함수 호출
        showNotification(title, content)

        return Result.success()
    }

    // 실제 알림을 표시하는 함수
    private fun showNotification(title: String, content: String) {
        val channelId = "checkIn_reminder"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "예약 알림 채널", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}


fun scheduleNotification(
    context: Context,
    title: String,
    content: String,
    delayInMinutes: Long = 10 // 기본값: 10분 후 알림
) {
    // Data 객체를 사용하여 Worker에 전달할 데이터를 정의합니다
    val data = Data.Builder()
        .putString("title", title)
        .putString("content", content)
        .build()

    // WorkRequest 생성: 특정 시간 후에 실행되도록 설정
    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delayInMinutes, TimeUnit.MINUTES) // 원하는 지연 시간 설정
        .setInputData(data) // 알림 데이터 전달
        .build()

    // WorkManager에 작업 예약
    WorkManager.getInstance(context).enqueue(workRequest)
}

