package com.itmo.moonphase.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.work.*
import com.itmo.moonphase.Consts
import com.itmo.moonphase.MoonPhaseEnum
import com.itmo.moonphase.Preferences
import com.itmo.moonphase.R
import com.itmo.moonphase.databinding.ActivitySettingsBinding
import com.itmo.moonphase.workers.MoonPhaseWorker
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializePreferences()
        initializeComponents()
    }

    private fun initializeComponents() = with(binding) {
        spnNotificationMoonPhases.adapter = ArrayAdapter(
            baseContext,
            R.layout.support_simple_spinner_dropdown_item,
            MoonPhaseEnum.values().map { getString(it.nameId) }
        ).apply {
            setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        }

        smNotification.setOnCheckedChangeListener { _, isChecked ->
            spnNotificationMoonPhases.isEnabled = isChecked
        }

        smNotification.isChecked = preferences.getBoolean(Preferences.Settings.IS_NOTIFICATION_ENABLED.name, false)
        spnNotificationMoonPhases.setSelection(
            preferences.getInt(Preferences.Settings.MOON_PHASE_TO_NOTIFY.name, MoonPhaseEnum.NEW_MOON.ordinal))
    }

    override fun onStop() {
        super.onStop()

        with (binding) {
            with(preferences.edit()) {
                putBoolean(Preferences.Settings.IS_NOTIFICATION_ENABLED.name, smNotification.isChecked)
                putInt(Preferences.Settings.MOON_PHASE_TO_NOTIFY.name, spnNotificationMoonPhases.selectedItemPosition)
                apply()
            }

            if (smNotification.isChecked) {
                initializeWorkers()
            }
        }
    }

    // SharedPreferences http://developer.alexanderklimov.ru/android/theory/sharedpreferences.php
    // Save key-value data https://developer.android.com/training/data-storage/shared-preferences
    private fun initializePreferences() {
        preferences = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE)
    }

    // Define work requests: Schedule periodic work https://developer.android.com/topic/libraries/architecture/workmanager/how-to/define-work#schedule_periodic_work
    private fun initializeWorkers() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<MoonPhaseWorker>(Consts.WORKER_PERIOD_HOURS, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        // Avoiding duplicating PeriodicWorkRequest from WorkManager https://stackoverflow.com/a/50943231
        WorkManager.getInstance(baseContext)
            .enqueueUniquePeriodicWork(Consts.WORKER_REQUEST_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    }

}
