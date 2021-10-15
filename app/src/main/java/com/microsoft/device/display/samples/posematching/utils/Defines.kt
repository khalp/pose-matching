package com.microsoft.device.display.samples.posematching.utils

object Defines {
    const val SMALLEST_TABLET_SCREEN_WIDTH_DP = 585

    // even with no toolbar, the hinge is offset by a default amount
    const val DEFAULT_TOOLBAR_OFFSET = 18

    // number of reference images to choose if the user wants to go with the default images
    const val DEFAULT_REFERENCES_PER_GAME = 4

    enum class GameState {
        FINISHED, STOPPED, PAUSED, RUNNING
    }
}