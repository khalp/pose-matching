package com.microsoft.device.display.samples.posematching

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ReactiveGuide
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoRepository
import androidx.window.layout.WindowInfoRepository.Companion.windowInfoRepository
import com.microsoft.device.display.samples.posematching.utils.Defines
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val viewModel: GameViewModel by viewModels()

    private lateinit var windowInfoRepo: WindowInfoRepository
    private var isAppSpanned: Boolean = false
    private var isTablet: Boolean = false
    private var isDualScreen: Boolean = false
    private var hingeSize: Int = 0
    private lateinit var hingeBounds: Rect
    private var isHingeVertical: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        windowInfoRepo = windowInfoRepository()
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                windowInfoRepo.windowLayoutInfo.collect { newLayoutInfo ->
                    for (displayFeature in newLayoutInfo.displayFeatures) {
                        val foldingFeature = displayFeature as? FoldingFeature

                        foldingFeature?.let { hinge ->
                            isAppSpanned = true
                            isHingeVertical =
                                hinge.orientation == FoldingFeature.Orientation.VERTICAL
                            hingeBounds = hinge.bounds
                            hingeSize =
                                if (isHingeVertical) hingeBounds.width() else hingeBounds.height()
                        }
                    }
                    val smallestScreenWidthDp =
                        applicationContext.resources.configuration.smallestScreenWidthDp
                    isTablet = smallestScreenWidthDp > Defines.SMALLEST_TABLET_SCREEN_WIDTH_DP
                    isDualScreen = isAppSpanned || isTablet

                    setupDualscreenUI()
                }
            }
        }
    }

    private fun setupDualscreenUI() {
        // set up bounds according to layout info
        viewModel.isDualScreen.value = isDualScreen
        if (isDualScreen) {
            val isDualPotrait = (isAppSpanned && isHingeVertical) ||
                    (isTablet && applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)

            if (isDualPotrait)
                setBoundsVerticalHinge()
            else
                setBoundsHorizontalHinge(hingeBounds)
        } else {
            setBoundsNoHinge()
        }


        /*// place fragments in view
        if (!supportFragmentManager.isDestroyed) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.primary_fragment_container, WelcomeFragment1())
                .commit()
            supportFragmentManager.beginTransaction()
                .replace(R.id.secondary_fragment_container, WelcomeFragment2())
                .commit()
        }*/
    }


    // -------------------------- Window Manager Section --------------------------- \\

    /**
     * Calculate total height taken up by upper toolbars
     * Add measurements here if additional status/toolbars are used
     */
    private fun upperToolbarSpacing(): Int {
        return Defines.DEFAULT_TOOLBAR_OFFSET
    }

    /**
     * Calculate the center offset between the guideline and the bounding box
     */
    private fun boundingOffset(height: Int): Int {
        return height / 2
    }

    /**
     * Set the bounding rectangle for a configuration with a vertical hinge
     */
    private fun setBoundsVerticalHinge() {
        val boundingRect: View = findViewById(R.id.bounding_rect)
        val params: ViewGroup.LayoutParams = boundingRect.layoutParams
        params.width = hingeSize
        boundingRect.layoutParams = params

        // left fragment is aligned with the right side of the hinge and vice-versa
        // add padding to ensure fragments do not overlap the hinge
        val leftFragment: FragmentContainerView = findViewById(R.id.primary_fragment_container)
        leftFragment.setPadding(0, 0, hingeSize, 0)

        val rightFragment: FragmentContainerView = findViewById(R.id.secondary_fragment_container)
        rightFragment.setPadding(hingeSize, 0, 0, 0)
        rightFragment.visibility = View.VISIBLE
    }

    /**
     * Set the bounding rectangle for a configuration with a horizontal hinge
     */
    private fun setBoundsHorizontalHinge(hingeBounds: Rect) {
        val boundingRect: View = findViewById(R.id.bounding_rect)
        val params: ViewGroup.LayoutParams = boundingRect.layoutParams
        params.height = hingeSize
        boundingRect.layoutParams = params

        val guide: ReactiveGuide = findViewById(R.id.horiz_guide)
        guide.setGuidelineBegin(hingeBounds.top + boundingOffset(hingeSize) - upperToolbarSpacing())

        // top fragment is aligned with the bottom side of the hinge and vice-versa
        // add padding to ensure fragments do not overlap the hinge
        val topFragment: FragmentContainerView = findViewById(R.id.primary_fragment_container)
        topFragment.setPadding(0, 0, 0, hingeSize)

        val bottomFragment: FragmentContainerView = findViewById(R.id.secondary_fragment_container)
        bottomFragment.setPadding(0, hingeSize, 0, 0)
        bottomFragment.visibility = View.VISIBLE
    }

    /**
     * Set the bounding rectangle for a configuration with no hinge (single screen)
     */
    private fun setBoundsNoHinge() {
        val boundingRect: View = findViewById(R.id.bounding_rect)
        val params: ViewGroup.LayoutParams = boundingRect.layoutParams

        // fill parent
        params.height = -1
        params.width = -1
        boundingRect.layoutParams = params

        val guide: ReactiveGuide = findViewById(R.id.horiz_guide)
        guide.setGuidelineEnd(0)
    }
}
