package com.gramavasathi.utils

import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.gramavasathi.R
import com.gramavasathi.ui.theme.*

fun Int.toScoreTier(): String = getScoreTier(this)

fun Int.toScoreColor(view: View): Int {
    val colorRes = when (this) {
        in 0..39 -> android.R.color.holo_red_light
        in 40..69 -> android.R.color.holo_orange_light
        in 70..89 -> R.color.golden_wheat
        else -> R.color.leaf_green
    }
    return ContextCompat.getColor(view.context, colorRes)
}

fun getScoreColor(score: Int): Color {
    return when (score) {
        in 0..39 -> Color(0xFFE57373)
        in 40..69 -> Color(0xFFFFB74D)
        in 70..89 -> GoldenWheat
        else -> LeafGreen
    }
}

fun getScoreTier(score: Int): String {
    return when (score) {
        in 0..39 -> "🌱 Getting Started"
        in 40..69 -> "🌿 Almost Ready"
        in 70..89 -> "🌾 Guest Ready"
        else -> "⭐ Certified Host"
    }
}
