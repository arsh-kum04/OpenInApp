package com.example.openinapp.utils

import android.graphics.drawable.GradientDrawable

object DrawableUtils {
    fun createGradientDrawable(startColor: Int, endColor: Int): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColor)
        )
    }
}