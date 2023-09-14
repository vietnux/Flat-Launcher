package net.tglt.android.fatlauncher.providers.color.theme

import androidx.core.graphics.ColorUtils
import androidx.core.graphics.luminance
import net.tglt.android.fatlauncher.providers.color.pallete.ColorPalette

class DarkColorTheme(
    val palette: ColorPalette,
) : ColorTheme {

    override val accentColor = palette.primary
    override val secondaryAccentColor = palette.secondary

    override val uiBG = palette.neutralVeryDark
    override val uiTitle = palette.neutralVeryLight
    override val uiDescription = palette.neutralLight
    override val uiHint = palette.neutralMedium

    override val cardBG = palette.neutralDark
    override val cardTitle = palette.neutralVeryLight
    override val cardDescription = palette.neutralLight
    override val cardHint = palette.neutralMedium

    override val separator = palette.neutralVeryLight and 0xffffff or 0x33000000

    override val buttonColor = palette.neutralMedium
    override val buttonColorCallToAction = palette.secondary

    override val searchBarBG = cardBG
    override val searchBarFG = cardTitle

    override fun adjustColorForContrast(base: Int, tint: Int): Int {
        return if (base.luminance > .7f) {
            val lab = DoubleArray(3)
            ColorUtils.colorToLAB(tint, lab)
            lab[0] = lab[0].coerceAtMost(20.0)
            ColorUtils.LABToColor(lab[0], lab[1], lab[2])
        } else {
            val lab = DoubleArray(3)
            ColorUtils.colorToLAB(tint, lab)
            lab[0] = 100.0
            lab[1] *= .75
            lab[2] *= .75
            ColorUtils.LABToColor(lab[0], lab[1], lab[2])
        }
    }
}