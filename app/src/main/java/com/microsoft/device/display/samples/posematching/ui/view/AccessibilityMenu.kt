package com.microsoft.device.display.samples.posematching.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.microsoft.device.display.samples.posematching.ui.theme.PoseMatchingTheme

@Composable
fun AccessibilityMenu() {
    var checkElbows by remember { mutableStateOf(true) }
    var checkShoulders by remember { mutableStateOf(true) }
    var checkKnees by remember { mutableStateOf(true) }
    var checkHips by remember { mutableStateOf(true) }

    PoseMatchingTheme {
        Column(
            modifier = Modifier.wrapContentWidth(),
            horizontalAlignment = Alignment.End
        ) {
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
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "check $label", color = MaterialTheme.colors.onSurface)
        Spacer(Modifier.width(10.dp))
        Switch(
            checked = checked,
            onCheckedChange = updateValue,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.secondary,
                checkedTrackColor = MaterialTheme.colors.secondary,
                uncheckedThumbColor = MaterialTheme.colors.secondary,
            ),
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