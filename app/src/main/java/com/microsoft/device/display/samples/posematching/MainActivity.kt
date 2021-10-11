package com.microsoft.device.display.samples.posematching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.layout.WindowInfoRepository
import androidx.window.layout.WindowInfoRepository.Companion.windowInfoRepository
import com.microsoft.device.display.samples.posematching.ui.Home
import com.microsoft.device.display.samples.posematching.ui.theme.PoseMatchingTheme

class MainActivity : ComponentActivity() {
    private lateinit var windowInfoRepo: WindowInfoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        windowInfoRepo = windowInfoRepository()

        setContent {
            PoseMatchingTheme {
                Home(windowInfoRepo)
            }
        }
    }
}
