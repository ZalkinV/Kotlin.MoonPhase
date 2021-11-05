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

    class MoonPhaseViewHolder(
        private val binding: ListItemMoonPhaseBinding,
        private val dateTimeFormatter: DateTimeFormatter,
        private val percentFormatter: NumberFormat,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moonPhaseInfo: MoonPhaseInfo) = binding.let { with(moonPhaseInfo) {
            it.tvMoonPhaseDate.text = dateTimeFormatter.format(dateTime)
            it.imgMoonPhase.setImageResource(getMoonPhaseImage(phase))
            it.tvMoonPhaseName.text = getMoonPhaseName(phase)
            it.tvMoonPhaseAge.text = age.roundToInt().toString()
            it.tvMoonPhaseIllumination.text = percentFormatter.format(illumination)
        } }

        private fun getMoonPhaseImage(moonPhase: MoonPhaseEnum) = when(moonPhase) {
            MoonPhaseEnum.NEW_MOON -> R.drawable.emoji_new_moon
            MoonPhaseEnum.WAXING_CRESCENT -> R.drawable.emoji_waxing_crescent
            MoonPhaseEnum.FIRST_QUARTER -> R.drawable.emoji_first_quarter
            MoonPhaseEnum.WAXING_GIBBOUS -> R.drawable.emoji_waxing_gibbous
            MoonPhaseEnum.FULL_MOON -> R.drawable.emoji_full_moon
            MoonPhaseEnum.WANING_GIBBOUS -> R.drawable.emoji_wanning_gibbous
            MoonPhaseEnum.LAST_QUARTER -> R.drawable.emoji_last_quarter
            MoonPhaseEnum.WANING_CRESCENT -> R.drawable.emoji_wanning_crescent
        }

        private fun getMoonPhaseName(moonPhase: MoonPhaseEnum) = with(binding.root.context) { when(moonPhase) {
            MoonPhaseEnum.NEW_MOON -> getString(R.string.moonPhase_newMoon)
            MoonPhaseEnum.WAXING_CRESCENT -> getString(R.string.moonPhase_waxingCrescent)
            MoonPhaseEnum.FIRST_QUARTER -> getString(R.string.moonPhase_firstQuarter)
            MoonPhaseEnum.WAXING_GIBBOUS -> getString(R.string.moonPhase_waxingGibbous)
            MoonPhaseEnum.FULL_MOON -> getString(R.string.moonPhase_fullMoon)
            MoonPhaseEnum.WANING_GIBBOUS -> getString(R.string.moonPhase_waningGibbous)
            MoonPhaseEnum.LAST_QUARTER -> getString(R.string.moonPhase_lastQuarter)
            MoonPhaseEnum.WANING_CRESCENT -> getString(R.string.moonPhase_waningCrescent)
        } }

    }

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
}
