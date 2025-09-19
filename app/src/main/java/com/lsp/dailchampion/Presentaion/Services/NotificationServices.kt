package com.lsp.dailchampion.Presentaion.Services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lsp.dailchampion.R
fun createNotificationChannel(context: Context,descriptionText:String ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Task Channel"
//        val descriptionText = "Show Task "
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel("task_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun showNotification(context: Context, title: String, message: String) {
    val channelId = "task_channel" // must match the one created above

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_background) // your app icon or custom drawable
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_LOW) // for < Android O
        .setAutoCancel(true)

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
}
