package com.itmo.moonphase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.itmo.moonphase.MoonPhaseEnum
import com.itmo.moonphase.R
import com.itmo.moonphase.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

}
