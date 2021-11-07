package com.itmo.moonphase.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.itmo.moonphase.*
import com.itmo.moonphase.api.farmsense.MoonPhaseProviderFarmsense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.RuntimeException

// Background work with WorkManager - Kotlin https://developer.android.com/codelabs/android-workmanager
class MoonPhaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private const val LOG_TAG = "MOON_WORKER"

        private const val NOTIFICATION_CHANNEL_NAME = "Moon Phase"
        private const val NOTIFICATION_CHANNEL_ID = "Moon_Phase_ChannelId"
        private const val NOTIFICATION_ID = 0
    }

    init {
        createNotificationChannel(context)
    }

    private val moonPhaseProvider: MoonPhaseProvider = MoonPhaseProviderFarmsense()

    override fun doWork(): Result {
        val appContext = applicationContext

        // TODO: read user preferences from notification settings
        val moonPhaseToNotify = MoonPhaseEnum.WANING_CRESCENT

        return try {
            CoroutineScope(Dispatchers.Main).launch {
                val currentDateTime = getDateTimeNow()
                val moonPhase = moonPhaseProvider.getMoonPhases(currentDateTime).first()

                if (moonPhase.phase == moonPhaseToNotify)
                    showNotification(appContext, moonPhase)
            }

            Result.success()
        } catch (e: RuntimeException) {
            e.log(LOG_TAG)
            Result.failure()
        }
    }

    private fun createNotificationChannel(context: Context) {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    // Create a Notification https://developer.android.com/training/notify-user/build-notification
    // Creating Notifications in Android Studio 2020 (Kotlin) https://www.youtube.com/watch?v=B5dgmvbrHgs
    // NOTIFICATIONS - Android Fundamentals https://www.youtube.com/watch?v=urn355_ymNA
    private fun showNotification(context: Context, moonPhase: MoonPhaseInfo) {
        val contentTitle = context.getString(R.string.notification_title_format)
            .format(context.getString(moonPhase.phase.nameId))

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(moonPhase.phase.imageId)
            .setContentTitle(contentTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

}