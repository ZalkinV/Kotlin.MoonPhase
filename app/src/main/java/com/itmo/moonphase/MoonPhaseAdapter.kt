package com.itmo.moonphase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itmo.moonphase.databinding.ListItemMoonPhaseBinding
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MoonPhaseAdapter(
    context: Context,
    private val moonPhases: List<MoonPhaseInfo>,
) : RecyclerView.Adapter<MoonPhaseAdapter.MoonPhaseViewHolder>() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.moonPhase_dateTime_format))
    private val percentFormatter = NumberFormat.getPercentInstance().apply { minimumFractionDigits = 0 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoonPhaseViewHolder {
        val binding = ListItemMoonPhaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return MoonPhaseViewHolder(binding, dateTimeFormatter, percentFormatter)
    }

    override fun onBindViewHolder(holder: MoonPhaseViewHolder, position: Int) =
        holder.bind(moonPhases[position])

    override fun getItemCount() = moonPhases.size

    class MoonPhaseViewHolder(
        private val binding: ListItemMoonPhaseBinding,
        private val dateTimeFormatter: DateTimeFormatter,
        private val percentFormatter: NumberFormat,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moonPhaseInfo: MoonPhaseInfo) = binding.let { with(moonPhaseInfo) {
            getMoonPhaseResource(phase).let { mpr ->
                it.imgMoonPhase.setImageResource(mpr.imageId)
                it.tvMoonPhaseName.text = mpr.name
            }

            it.tvMoonPhaseDate.text = dateTimeFormatter.format(dateTime)
            it.tvMoonPhaseAge.text = age.roundToInt().toString()
            it.tvMoonPhaseIllumination.text = percentFormatter.format(illumination)
        } }

        private fun getMoonPhaseResource(moonPhase: MoonPhaseEnum) = with(binding.root.context) { when(moonPhase) {
            MoonPhaseEnum.NEW_MOON -> MoonPhaseResource(R.drawable.emoji_new_moon, getString(R.string.moonPhase_newMoon))
            MoonPhaseEnum.WAXING_CRESCENT -> MoonPhaseResource(R.drawable.emoji_waxing_crescent, getString(R.string.moonPhase_waxingCrescent))
            MoonPhaseEnum.FIRST_QUARTER -> MoonPhaseResource(R.drawable.emoji_first_quarter, getString(R.string.moonPhase_firstQuarter))
            MoonPhaseEnum.WAXING_GIBBOUS -> MoonPhaseResource(R.drawable.emoji_waxing_gibbous, getString(R.string.moonPhase_waxingGibbous))
            MoonPhaseEnum.FULL_MOON -> MoonPhaseResource(R.drawable.emoji_full_moon, getString(R.string.moonPhase_fullMoon))
            MoonPhaseEnum.WANING_GIBBOUS -> MoonPhaseResource(R.drawable.emoji_wanning_gibbous, getString(R.string.moonPhase_waningGibbous))
            MoonPhaseEnum.LAST_QUARTER -> MoonPhaseResource(R.drawable.emoji_last_quarter, getString(R.string.moonPhase_lastQuarter))
            MoonPhaseEnum.WANING_CRESCENT -> MoonPhaseResource(R.drawable.emoji_wanning_crescent, getString(R.string.moonPhase_waningCrescent))
        } }

        data class MoonPhaseResource(val imageId: Int, val name: String)

    }
}
