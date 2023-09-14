package net.tglt.android.fatlauncher.ui.intro

import android.os.Bundle
import android.view.View
import android.widget.TextView
import net.tglt.android.fatlauncher.R
import net.tglt.android.fatlauncher.providers.color.theme.ColorTheme

class SplashFragment : FragmentWithNext(R.layout.intro_splash) {
    override fun next(activity: IntroActivity) {
        activity.setFragment(PermissionsFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.title).setTextColor(ColorTheme.uiTitle)
    }
}