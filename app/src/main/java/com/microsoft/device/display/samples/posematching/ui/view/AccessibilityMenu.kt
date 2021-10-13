package com.microsoft.device.display.samples.posematching.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.microsoft.device.display.samples.posematching.ui.theme.PoseMatchingTheme

@Composable
fun AccessibilityMenu() {
    var checkElbows by remember { mutableStateOf(false) }
    var checkShoulders by remember { mutableStateOf(false) }
    var checkKnees by remember { mutableStateOf(false) }
    var checkHips by remember { mutableStateOf(false) }

    PoseMatchingTheme {
        Column {
            SwitchWithLabel("elbows", checkElbows) { value -> checkElbows = value }
            SwitchWithLabel("shoulders", checkShoulders) { value -> checkShoulders = value }
            SwitchWithLabel("knees", checkKnees) { value -> checkKnees = value }
            SwitchWithLabel("hips", checkHips) { value -> checkHips = value }
        }
    }
}

@Composable
private fun SwitchWithLabel(label: String, checked: Boolean, updateValue: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "check $label", color = MaterialTheme.colors.onSurface)
        Switch(
            checked = checked,
            onCheckedChange = updateValue,
        )
    }
}

@Preview
@Composable
private fun PreviewMenu() {
    PoseMatchingTheme {
        AccessibilityMenu()
    }
}