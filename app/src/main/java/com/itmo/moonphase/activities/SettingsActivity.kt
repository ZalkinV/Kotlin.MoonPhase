package com.itmo.moonphase.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.itmo.moonphase.MoonPhaseEnum
import com.itmo.moonphase.Preferences
import com.itmo.moonphase.R
import com.itmo.moonphase.databinding.ActivitySettingsBinding

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

        smNotification.setOnCheckedChangeListener { _, isChecked -> spnNotificationMoonPhases.isEnabled = isChecked }

        smNotification.isChecked = preferences.getBoolean(Preferences.Settings.IS_NOTIFICATION_ENABLED.name, false)
        spnNotificationMoonPhases.setSelection(
            preferences.getInt(Preferences.Settings.MOON_PHASE_TO_NOTIFY.name, MoonPhaseEnum.NEW_MOON.ordinal))
    }

    override fun onStop() {
        super.onStop()

        with(preferences.edit()) { with (binding) {
            putBoolean(Preferences.Settings.IS_NOTIFICATION_ENABLED.name, smNotification.isChecked)
            putInt(Preferences.Settings.MOON_PHASE_TO_NOTIFY.name, spnNotificationMoonPhases.selectedItemPosition)
            apply()
        } }
    }

    // SharedPreferences http://developer.alexanderklimov.ru/android/theory/sharedpreferences.php
    // Save key-value data https://developer.android.com/training/data-storage/shared-preferences
    private fun initializePreferences() {
        preferences = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE)
    }

}
