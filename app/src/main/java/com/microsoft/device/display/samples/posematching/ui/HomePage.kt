package com.microsoft.device.display.samples.posematching.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoRepository
import com.microsoft.device.display.samples.posematching.ui.view.CameraView
import com.microsoft.device.display.samples.posematching.ui.view.ReferenceView
import com.microsoft.device.display.samples.posematching.utils.Defines
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneLayout
import kotlinx.coroutines.flow.collect



@Composable
fun Home(windowInfoRepo: WindowInfoRepository) {
    // Set up variables to keep track of foldable info (may not use all of these, can delete later)
    var isAppSpanned by remember { mutableStateOf(false) }
    var isHingeVertical by remember { mutableStateOf(false) }
    var hingeSize by remember { mutableStateOf(0) }

    LaunchedEffect(windowInfoRepo) {
        windowInfoRepo.windowLayoutInfo
            .collect { newLayoutInfo ->
                val displayFeatures = newLayoutInfo.displayFeatures
                isAppSpanned = displayFeatures.isNotEmpty()
                if (isAppSpanned) {
                    val foldingFeature = displayFeatures.first() as FoldingFeature
                    val bounds = foldingFeature.bounds

                    isHingeVertical =
                        foldingFeature.orientation == FoldingFeature.Orientation.VERTICAL
                    hingeSize = if (isHingeVertical) bounds.width() else bounds.height()
                }
            }
    }

    val smallestScreenWidthDp = LocalConfiguration.current.smallestScreenWidthDp
    val isTablet = smallestScreenWidthDp > Defines.SMALLEST_TABLET_SCREEN_WIDTH_DP
    val isDualScreen = (isAppSpanned || isTablet)
    val isDualPortrait = when {
        isAppSpanned -> isHingeVertical
        isTablet -> LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        else -> false
    }
    val isDualLandscape = when {
        isAppSpanned -> !isHingeVertical
        isTablet -> LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
        else -> false
    }

    TwoPaneLayout(
        pane1 = { ReferenceView() },
        pane2 = { CameraView() },
    )
}