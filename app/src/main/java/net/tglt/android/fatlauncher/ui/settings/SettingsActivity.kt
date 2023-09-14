package net.tglt.android.fatlauncher.ui.settings

import android.app.Activity
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.ui.home.main.acrylicBlur
import net.tglt.android.fatlauncher.util.drawable.FastColorDrawable
import net.tglt.android.fatlauncher.util.storage.Settings

abstract class SettingsActivity : Activity() {

    val settings = Settings()

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) window.setDecorFitsSystemWindows(false)
        else window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        settings.init(applicationContext)
        loadColors()

        init(savedInstanceState)
    }

    abstract fun init(savedInstanceState: Bundle?)

    private fun loadColors() {
        window.decorView.background = LayerDrawable(arrayOf(
            acrylicBlur?.fullBlurDrawable,
            FastColorDrawable(ColorTheme.uiBG),
        ))
    }
}