package com.microsoft.device.display.samples.posematching.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.microsoft.device.display.samples.posematching.ui.theme.PoseMatchingTheme

@Composable
fun CountdownText(text: String) {
    PoseMatchingTheme {
        Box(
            modifier = Modifier.background(
                MaterialTheme.colors.surface.copy(0.7f),
                RoundedCornerShape(percent = 15)
            ).sizeIn(100.dp, 100.dp, 220.dp, 100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                text = text,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h1
            )
            Text(
                text = text,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h1
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun PreviewCountdownText() {
    CountdownText("POSE!")
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PreviewCountdownTextDark() {
    CountdownText("POSE!")
}