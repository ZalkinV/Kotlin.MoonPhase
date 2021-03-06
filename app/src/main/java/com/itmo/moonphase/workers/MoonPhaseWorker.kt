package com.itmo.moonphase.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.itmo.moonphase.*
import com.itmo.moonphase.activities.MainActivity
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
    private lateinit var preferences: SharedPreferences


    override fun doWork(): Result {
        val appContext = applicationContext

        initializePreferences(appContext)

        val isNotificationEnabled = preferences.getBoolean(Preferences.Settings.IS_NOTIFICATION_ENABLED.name, false)
        if (!isNotificationEnabled) return Result.success()

        val moonPhaseToNotifyOrdinal = preferences.getInt(Preferences.Settings.MOON_PHASE_TO_NOTIFY.name, 0)

        return try {
            CoroutineScope(Dispatchers.Main).launch {
                val currentDateTime = getDateTimeNow()
                val moonPhase = moonPhaseProvider.getMoonPhases(currentDateTime).first()

                if (moonPhase.phase.ordinal == moonPhaseToNotifyOrdinal)
                    showNotification(appContext, moonPhase)
            }

            Result.success()
        } catch (e: RuntimeException) {
            e.log(LOG_TAG)
            Result.failure()
        }
    }

    private fun initializePreferences(context: Context) {
        preferences = context.getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE)
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

        // Notification click: activity already open https://stackoverflow.com/a/12043752
        // Activity opened twice https://stackoverflow.com/a/16332324
        val openAppIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java)
                .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP },
            PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(moonPhase.phase.imageId)
            .setContentTitle(contentTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

}