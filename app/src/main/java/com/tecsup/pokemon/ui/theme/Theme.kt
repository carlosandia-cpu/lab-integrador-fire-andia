package com.tecsup.pokemon.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PokemonRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD4),
    onPrimaryContainer = Color(0xFF3B0900),
    secondary = PokemonBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7E2FF),
    onSecondaryContainer = Color(0xFF001B3E),
    tertiary = PokemonGold,
    background = PokemonBackgroundLight,
    surface = PokemonSurfaceLight,
    onSurface = Color(0xFF1C1B1F),
    onSurfaceVariant = Color(0xFF44474F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB4A9),
    onPrimary = Color(0xFF680000),
    primaryContainer = Color(0xFF930000),
    onPrimaryContainer = Color(0xFFFFDAD4),
    secondary = Color(0xFFAEC6FF),
    onSecondary = Color(0xFF002C6F),
    secondaryContainer = Color(0xFF00419E),
    onSecondaryContainer = Color(0xFFD7E2FF),
    tertiary = PokemonGold,
    background = PokemonBackgroundDark,
    surface = PokemonSurfaceDark,
    onSurface = Color(0xFFE2E2E6),
    onSurfaceVariant = Color(0xFFC4C6D0)
)

@Composable
fun PokemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Prioritize Pokémon brand identity colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
