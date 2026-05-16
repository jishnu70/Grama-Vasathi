package com.gramavasathi.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.gramavasathi.R

fun Int.toScoreTier(): String {
    return when (this) {
        in 0..39 -> "🌱 Getting Started"
        in 40..69 -> "🌿 Almost Ready"
        in 70..89 -> "🌾 Guest Ready"
        else -> "⭐ Certified Host"
    }
}

fun Int.toScoreColor(view: View): Int {
    val colorRes = when (this) {
        in 0..39 -> android.R.color.holo_red_light
        in 40..69 -> android.R.color.holo_orange_light
        in 70..89 -> R.color.golden_wheat
        else -> R.color.leaf_green
    }
    return ContextCompat.getColor(view.context, colorRes)
}
