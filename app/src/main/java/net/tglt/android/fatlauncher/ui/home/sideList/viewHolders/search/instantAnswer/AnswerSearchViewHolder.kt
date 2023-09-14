package net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search.instantAnswer

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.data.search.InstantAnswerResult
import net.tglt.android.fatlauncher.data.search.SearchResult
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme
import net.tglt.android.fatlauncher.ui.home.MainActivity
import net.tglt.android.fatlauncher.ui.home.main.acrylicBlur
import net.tglt.android.fatlauncher.ui.home.sideList.viewHolders.search.SearchViewHolder
import net.tglt.android.fatlauncher.ui.view.SeeThroughView
import net.tglt.android.fatlauncher.util.drawable.setBackgroundColorFast

class AnswerSearchViewHolder(
    itemView: View
) : SearchViewHolder(itemView) {

    val card = itemView.findViewById<CardView>(R.id.card)!!
    val container = card.findViewById<View>(R.id.container)!!
    val title = container.findViewById<TextView>(R.id.title)!!
    val description = container.findViewById<TextView>(R.id.description)!!

    private val infoBoxAdapter = InfoBoxAdapter()
    private val infoBox = itemView.findViewById<RecyclerView>(R.id.info_box)!!.apply {
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = infoBoxAdapter
    }

    private val actionsContainer = itemView.findViewById<CardView>(R.id.actions_container)!!
    private val sourceAction = actionsContainer.findViewById<TextView>(R.id.source)!!
    private val searchAction = actionsContainer.findViewById<TextView>(R.id.search)!!
    private val actionSeparator = actionsContainer.findViewById<View>(R.id.separator)!!

    private val blurBG = itemView.findViewById<SeeThroughView>(R.id.blur_bg)!!.apply {
        viewTreeObserver.addOnPreDrawListener {
            invalidate()
            true
        }
    }

    override fun onBind(
        result: SearchResult,
        activity: MainActivity,
    ) {
        result as InstantAnswerResult

        blurBG.drawable = acrylicBlur?.smoothBlurDrawable
        blurBG.offset = 1f
        activity.setOnPageScrollListener(AnswerSearchViewHolder::class.simpleName!!) { blurBG.offset = it }

        card.setCardBackgroundColor(ColorTheme.cardBG)
        container.backgroundTintList = ColorStateList.valueOf(ColorTheme.separator)
        title.setTextColor(ColorTheme.cardTitle)
        description.setTextColor(ColorTheme.cardDescription)

        title.text = result.title
        description.text = result.description
        sourceAction.text = itemView.context.getString(R.string.read_more_at_source, result.sourceName)

        actionsContainer.setCardBackgroundColor(ColorTheme.buttonColor)
        searchAction.setTextColor(ColorTheme.titleColorForBG(ColorTheme.buttonColor))
        actionSeparator.setBackgroundColorFast(ColorTheme.hintColorForBG(ColorTheme.buttonColor))

        sourceAction.setTextColor(ColorTheme.titleColorForBG(ColorTheme.buttonColorCallToAction))
        sourceAction.setBackgroundColorFast(ColorTheme.buttonColorCallToAction)

        sourceAction.setOnClickListener(result::open)
        searchAction.setOnClickListener(result::search)

        if (result.infoTable == null) {
            infoBox.isVisible = false
        } else {
            infoBox.isVisible = true
            infoBoxAdapter.updateEntries(result.infoTable)
        }
    }
}