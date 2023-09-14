package net.tglt.android.fatlauncher.providers.color

import net.tglt.android.fatlauncher.providers.color.pallete.ColorPalette
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.providers.color.theme.DarkColorTheme
import net.tglt.android.fatlauncher.providers.color.theme.LightColorTheme

data class ColorThemeOptions(
    val mode: DayNight
) {
    enum class DayNight {
        AUTO,
        DARK,
        LIGHT,
    }

    fun createColorTheme(palette: ColorPalette): ColorTheme {
        return if (mode == DayNight.LIGHT) LightColorTheme(palette)
        else DarkColorTheme(palette)
    }

    override fun toString() = "${javaClass.simpleName} { mode: $mode }"
}