package com.turkcell.lyraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.turkcell.lyraapp.ui.navigation.LyraNavGraph
import com.turkcell.lyraapp.ui.theme.LocalThemeController
import com.turkcell.lyraapp.ui.theme.LyraTheme
import com.turkcell.lyraapp.ui.theme.ThemeController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            CompositionLocalProvider(
                LocalThemeController provides ThemeController(
                    isDarkTheme = isDarkTheme,
                    setDarkTheme = { isDarkTheme = it },
                )
            ) {
                LyraTheme(darkTheme = isDarkTheme) {
                    LyraNavGraph()
                }
            }
        }
    }
}
