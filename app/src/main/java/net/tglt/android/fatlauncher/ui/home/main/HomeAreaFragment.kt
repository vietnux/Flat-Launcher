package net.tglt.android.fatlauncher.ui.home.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import net.tglt.android.fatlauncher.LauncherContext
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.ui.home.MainActivity
import net.tglt.android.fatlauncher.util.blur.AcrylicBlur
import net.tglt.android.fatlauncher.util.storage.*
import net.tglt.android.fatlauncher.util.storage.DoBlurSetting.doBlur
import io.posidon.android.conveniencelib.*
import net.tglt.android.fatlauncher.providers.suggestions.SuggestionsManager
import kotlin.concurrent.thread

var acrylicBlur: AcrylicBlur? = null
    private set

class HomeAreaFragment : Fragment() {

    private lateinit var homeArea: HomeArea
    private lateinit var launcherContext: LauncherContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a = requireActivity() as MainActivity
        launcherContext = a.launcherContext
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.activity_launcher, container, false).apply {
        homeArea = HomeArea(this as NestedScrollView, this@HomeAreaFragment, launcherContext)

        val a = requireActivity() as MainActivity
        a.setOnColorThemeUpdateListener(HomeAreaFragment::class.simpleName!!, ::updateColorTheme)
        a.setOnBlurUpdateListener(HomeAreaFragment::class.simpleName!!, ::updateBlur)
        a.setOnAppsLoadedListener(HomeAreaFragment::class.simpleName!!) {
            a.runOnUiThread(homeArea::updatePinned)
        }
        a.setOnGraphicsLoaderChangeListener(HomeAreaFragment::class.simpleName!!) {
            a.runOnUiThread(homeArea::forceUpdatePinned)
        }
        a.setOnPageScrollListener(HomeAreaFragment::class.simpleName!!, ::onOffsetUpdate)
        a.setOnLayoutChangeListener(HomeAreaFragment::class.simpleName!!, homeArea::updateLayout)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateBlur()
        updateColorTheme()
        configureWindow()
        view.viewTreeObserver?.addOnWindowFocusChangeListener(::onWindowFocusChanged)
    }

    fun onWindowFocusChanged(hasFocus: Boolean) {
        homeArea.onWindowFocusChanged(hasFocus)
    }

    override fun onResume() {
        super.onResume()
        homeArea.dash.onResume()
        val a = requireActivity() as MainActivity
        SuggestionsManager.onResume(a) {
            a.runOnUiThread {
                homeArea.updateSuggestions(launcherContext.appManager.pinnedItems)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configureWindow()
    }

    private fun configureWindow() {
        val t = resources.getDimension(R.dimen.item_card_margin).toInt()
        homeArea.pinnedRecycler.doOnLayout {
            val b = (t + (requireActivity() as MainActivity).getSearchBarInset()) / 2
            homeArea.pinnedRecycler.setPadding(t, 0, t, b)
            homeArea.view.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).apply {
                bottomMargin = b
            }
        }
        homeArea.dash.view.setPadding(0, requireContext().getStatusBarHeight(), 0, 0)
        homeArea.updateLayout()
    }

    private fun updateBlur() {
        activity?.runOnUiThread(homeArea::updateBlur)
    }

    private fun updateColorTheme() {
        activity?.runOnUiThread(homeArea::updateColorTheme)
    }

    private fun onOffsetUpdate(offset: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val r = offset * 18f
            view?.setRenderEffect(
                if (r == 0f) null else
                    RenderEffect.createBlurEffect(r, r, Shader.TileMode.CLAMP)
            )
        }
        val i = (1 - offset * offset)
        view?.alpha = i * 1.1f
    }
}

fun Activity.loadBlur(settings: Settings, wallpaperManager: WallpaperManager, updateBlur: () -> Unit) = thread(isDaemon = true, name = "Blur thread") {
    if (!settings.doBlur || ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) != PackageManager.PERMISSION_GRANTED) {
        acrylicBlur ?: return@thread
        acrylicBlur = null
        updateBlur()
        return@thread
    }
    val drawable = wallpaperManager.peekDrawable()
    if (drawable == null) {
        acrylicBlur ?: return@thread
        acrylicBlur = null
        updateBlur()
        return@thread
    }
    AcrylicBlur.blurWallpaper(this, drawable) {
        acrylicBlur = it
        updateBlur()
    }
}