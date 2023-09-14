package net.tglt.android.fatlauncher.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import net.tglt.android.fatlauncher.providers.color.pallete.ColorPalette
import io.posidon.ksugar.delegates.observable
import kotlin.properties.Delegates

class ColorPaletteTestView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var palette: ColorPalette? by Delegates.observable { _ -> invalidate() }

    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        val palette = palette ?: return
        val all = arrayOf(
            palette.neutralVeryDark,
            palette.neutralDark,
            palette.neutralMedium,
            palette.neutralLight,
            palette.neutralVeryLight,
            palette.primary,
            palette.secondary,
        )
        val w = width.toFloat() / all.size
        for (i in all.indices) {
            paint.color = all[i]
            canvas.drawRect(i * w, 0f, (i + 1) * w, height.toFloat(), paint)
        }
    }
}