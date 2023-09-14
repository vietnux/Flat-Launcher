package net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.data.search.MathResult
import net.tglt.android.fatlauncher.data.search.SearchResult
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.ui.home.MainActivity
import net.tglt.android.fatlauncher.ui.home.pinned.acrylicBlur
import net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search.instantAnswer.AnswerSearchViewHolder
import net.tglt.android.fatlauncher.util.view.SeeThroughView

class SimpleBoxSearchViewHolder(
    itemView: View
) : SearchViewHolder(itemView) {

    val text = itemView.findViewById<TextView>(R.id.text)!!
    val card = itemView.findViewById<CardView>(R.id.card)!!

    val blurBG = itemView.findViewById<SeeThroughView>(R.id.blur_bg)!!.apply {
        viewTreeObserver.addOnPreDrawListener {
            invalidate()
            true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(
        result: SearchResult,
        activity: MainActivity,
    ) {
        result as MathResult

        blurBG.drawable = acrylicBlur?.smoothBlurDrawable
        blurBG.offset = 1f
        activity.setOnPageScrollListener(AnswerSearchViewHolder::class.simpleName!!) { blurBG.offset = it }

        card.setCardBackgroundColor(ColorTheme.cardBG)
        text.setTextColor(ColorTheme.cardTitle)

        text.text = "${result.operation} = ${result.result}"
        itemView.setOnClickListener(result::open)
    }
}