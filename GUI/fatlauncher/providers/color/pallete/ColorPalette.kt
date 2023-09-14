package net.tglt.android.fatlauncher.providers.color.pallete

import android.Manifest
import android.app.WallpaperColors
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import net.tglt.android.fatlauncher.util.storage.ColorExtractorSetting
import posidon.android.conveniencelib.Device
import kotlin.math.min

interface ColorPalette {
    val neutralVeryDark: Int
    val neutralDark: Int
    val neutralMedium: Int
    val neutralLight: Int
    val neutralVeryLight: Int

    val primary: Int
    val secondary: Int

    companion object {
        var wallColor: Int = 0
            private set

        private var colorPaletteInstance: ColorPalette = DefaultPalette

        fun <A : Context> loadWallColorTheme(context: A, onFinished: (A, ColorPalette) -> Unit) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                loadDefaultColorTheme(context, onFinished)
                return
            }
            val wp = context.getSystemService(WallpaperManager::class.java)
            val d = wp.fastDrawable
            val wall = d.toBitmap(
                min(d.intrinsicWidth, Device.screenWidth(context) / 4),
                min(d.intrinsicHeight, Device.screenHeight(context) / 4)
            )
            val palette = Palette.from(wall)
                .maximumColorCount(20)
                .generate()
            wallColor = palette.getDominantColor(0)
            val newColorTheme = BitmapBasedPalette(palette)
            if (newColorTheme != colorPaletteInstance) {
                colorPaletteInstance = newColorTheme
                onFinished(context, colorPaletteInstance)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O_MR1)
        fun <A : Context> loadSystemWallColorTheme(
            context: A,
            onFinished: (A, ColorPalette) -> Unit,
            colors: WallpaperColors
        ) {
            wallColor = colors.primaryColor.toArgb()
            colorPaletteInstance = AndroidOMR1Palette(context, colors)
            onFinished(context, colorPaletteInstance)
        }

        @RequiresApi(Build.VERSION_CODES.S)
        fun <A : Context> loadMonetColorTheme(
            context: A,
            onFinished: (A, ColorPalette) -> Unit,
        ) {
            colorPaletteInstance = MonetPalette(context.resources)
            onFinished(context, colorPaletteInstance)
        }

        private fun <A : Context> loadDefaultColorTheme(context: A, onFinished: (A, ColorPalette) -> Unit) {
            wallColor = 0
            if (colorPaletteInstance !== DefaultPalette) {
                colorPaletteInstance = DefaultPalette
                onFinished(context, colorPaletteInstance)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O_MR1)
        fun <A : Context> onColorsChanged(
            context: A,
            colorTheme: Int,
            onFinished: (A, ColorPalette) -> Unit,
            colors: () -> WallpaperColors?,
        ) {
            when (colorTheme) {
                ColorExtractorSetting.COLOR_THEME_WALLPAPER_TINT -> loadWallColorTheme(context, onFinished)
                ColorExtractorSetting.COLOR_THEME_WALLPAPER_TINT_SYSTEM_ASSISTED -> colors()?.let {
                    loadSystemWallColorTheme(context, onFinished, it)
                } ?: loadDefaultColorTheme(context, onFinished)
                ColorExtractorSetting.COLOR_THEME_MONET -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    loadMonetColorTheme(context, onFinished)
                } else loadDefaultColorTheme(context, onFinished)
                else -> loadDefaultColorTheme(context, onFinished)
            }
        }

        fun <A : Context> onResumePreOMR1(
            context: A,
            colorTheme: Int,
            onFinished: (A, ColorPalette) -> Unit,
        ) {
            when (colorTheme) {
                ColorExtractorSetting.COLOR_THEME_MONET,
                ColorExtractorSetting.COLOR_THEME_WALLPAPER_TINT_SYSTEM_ASSISTED,
                ColorExtractorSetting.COLOR_THEME_WALLPAPER_TINT -> loadWallColorTheme(context, onFinished)
                else -> loadDefaultColorTheme(context, onFinished)
            }
        }

        fun getCurrent() = colorPaletteInstance
    }
}