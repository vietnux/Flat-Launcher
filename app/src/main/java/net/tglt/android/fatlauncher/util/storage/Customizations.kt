package net.tglt.android.fatlauncher.util.storage

import net.tglt.android.fatlauncher.providers.color.ColorThemeOptions

object ColorExtractorSetting {
    val Settings.colorTheme: Int
        get() = get(KEY_COLOR_THEME, DEFAULT)

    var Settings.SettingsEditor.colorTheme: Int
        get() = settings[KEY_COLOR_THEME, DEFAULT]
        set(value) = KEY_COLOR_THEME set value

    private const val KEY_COLOR_THEME = "color_theme"

    const val COLOR_THEME_PLAIN = 0
    const val COLOR_THEME_WALLPAPER_TINT = 1
    const val COLOR_THEME_WALLPAPER_TINT_SYSTEM_ASSISTED = 2
    const val COLOR_THEME_MONET = 3

    const val DEFAULT = COLOR_THEME_WALLPAPER_TINT
}

object ColorThemeSetting {
    val Settings.colorThemeDayNight: ColorThemeOptions.DayNight
        get() = ColorThemeOptions.DayNight.values()[get(KEY, DEFAULT)]

    var Settings.SettingsEditor.colorThemeDayNight: ColorThemeOptions.DayNight
        get() = ColorThemeOptions.DayNight.values()[settings[KEY, DEFAULT]]
        inline set(value) = setColorThemeDayNight(value.ordinal)

    fun Settings.SettingsEditor.setColorThemeDayNight(i: Int) { KEY set i }

    private const val KEY = "color_theme:day_night"

    const val DEFAULT = 0
}

object DoReshapeAdaptiveIconsSetting {

    inline val Settings.forceReshapeAdaptiveIcons: Boolean
        get() = adaptiveIconsReshaping == FORCE

    inline val Settings.doReshapeAdaptiveIcons: Boolean
        get() = adaptiveIconsReshaping != NONE

    val Settings.adaptiveIconsReshaping: Int
        get() = get(KEY, DEFAULT)

    var Settings.SettingsEditor.adaptiveIconsReshaping: Int
        get() = settings[KEY, DEFAULT]
        set(value) = KEY set value

    private const val KEY = "icons:reshape_adaptive"

    const val NONE = 0
    const val SAFE = 1
    const val FORCE = 2

    const val DEFAULT = NONE
}

object DoMonochromeIconsSetting {

    val Settings.doMonochromeTileBackground: Boolean
        get() = monochromatism != NONE

    inline val Settings.doMonochromeIcons: Boolean
        get() = monochromatism == FULL

    val Settings.monochromatism: Int
        get() = get(KEY, DEFAULT)

    var Settings.SettingsEditor.monochromatism: Int
        get() = settings[KEY, DEFAULT]
        set(value) = KEY set value

    private const val KEY = "monochromatism"

    const val NONE = 0
    const val TILE_BACKGROUND = 1
    const val FULL = 2

    const val DEFAULT = NONE
}

object DoBlurSetting {
    val Settings.doBlur: Boolean
        get() = get(KEY, DEFAULT)

    var Settings.SettingsEditor.doBlur: Boolean
        get() = settings[KEY, DEFAULT]
        set(value) = KEY set value

    private const val KEY = "blur"

    const val DEFAULT = true
}

object DoShowKeyboardOnAllAppsScreenOpenedSetting {
    val Settings.doAutoKeyboardInAllApps: Boolean
        get() = get(KEY, DEFAULT)

    var Settings.SettingsEditor.doAutoKeyboardInAllApps: Boolean
        get() = settings[KEY, DEFAULT]
        set(value) = KEY set value

    private const val KEY = "keyboard:auto_show_in_all_apps"

    const val DEFAULT = false
}

object DoSuggestionStripSetting {
    val Settings.doSuggestionStrip: Boolean
        get() = get(KEY, DEFAULT)

    var Settings.SettingsEditor.doSuggestionStrip: Boolean
        get() = settings[KEY, DEFAULT]
        set(value) = KEY set value

    private const val KEY = "suggestions:show"

    const val DEFAULT = true
}