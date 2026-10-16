package com.example.laboratorio09.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = VerdeTinta,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = Tinta,
    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = CoralClaro,
    onSecondaryContainer = Tinta,
    background = Papel,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = VerdeClaro,
    onSurfaceVariant = TintaSecundaria,
    outline = Color(0xFFABBDB9),
    outlineVariant = Color(0xFFCAD8D3)
)

private val DarkColorScheme = darkColorScheme(
    primary = VerdeLuz,
    onPrimary = VerdeNoche,
    primaryContainer = Color(0xFF28524E),
    onPrimaryContainer = Color(0xFFD5F2EC),
    secondary = CoralLuz,
    onSecondary = Color(0xFF4A2018),
    secondaryContainer = Color(0xFF64352B),
    onSecondaryContainer = Color(0xFFFFDAD0),
    background = VerdeNoche,
    onBackground = Color(0xFFE6F1EE),
    surface = SuperficieNoche,
    onSurface = Color(0xFFE6F1EE),
    surfaceVariant = Color(0xFF294346),
    onSurfaceVariant = Color(0xFFB6CDCA),
    outline = Color(0xFF78918E),
    outlineVariant = Color(0xFF49615E)
)

@Composable
fun Laboratorio09Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val activity = LocalContext.current.findActivity()
    SideEffect {
        activity?.window?.let { window ->
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
