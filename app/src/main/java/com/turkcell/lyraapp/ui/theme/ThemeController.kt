package com.turkcell.lyraapp.ui.theme

import androidx.compose.runtime.compositionLocalOf

data class ThemeController(
    val isDarkTheme: Boolean,
    val setDarkTheme: (Boolean) -> Unit,
)

val LocalThemeController = compositionLocalOf<ThemeController> {
    error("LocalThemeController not provided")
}
