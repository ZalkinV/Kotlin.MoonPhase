package com.itmo.moonphase

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.itmo.moonphase.api.farmsense.MoonPhaseProviderFarmsense
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Android App Widgets Tutorial (Coffee Log) https://www.raywenderlich.com/33-android-app-widgets-tutorial
class MainAppWidget : AppWidgetProvider() {

    private val moonPhaseProvider = MoonPhaseProviderFarmsense()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, moonPhaseProvider)
        }
    }

}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    moonPhaseProvider: MoonPhaseProvider
) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.moonPhase_dateTime_format))

    val views = RemoteViews(context.packageName, R.layout.main_app_widget)

    GlobalScope.launch {
        val currentDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val moonPhases = moonPhaseProvider.getMoonPhases(currentDateTime)

        val moonPhase = moonPhases[0]
        views.setTextViewText(R.id.wd_tv_date, dateTimeFormatter.format(moonPhase.dateTime))
        views.setImageViewResource(R.id.wd_img_moonPhase, MoonPhaseResource.getMoonPhaseResource(moonPhase.phase).imageId)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}