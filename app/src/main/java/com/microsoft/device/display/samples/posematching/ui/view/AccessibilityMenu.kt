package com.microsoft.device.display.samples.posematching.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData

@ExperimentalFoundationApi
@Composable
fun AccessibilityMenu(
    checkElbows: LiveData<Boolean>,
    updateElbows: (Boolean) -> Unit,
    checkShoulders: LiveData<Boolean>,
    updateShoulders: (Boolean) -> Unit,
    checkKnees: LiveData<Boolean>,
    updateKnees: (Boolean) -> Unit,
    checkHips: LiveData<Boolean>,
    updateHips: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(true) }

    data class Joint(var check: LiveData<Boolean>, val update: (Boolean) -> Unit, val label: String)

    val joints = listOf(
        Joint(checkElbows, updateElbows, "elbow"),
        Joint(checkShoulders, updateShoulders, "shoulder"),
        Joint(checkHips, updateHips, "hip"),
        Joint(checkKnees, updateKnees, "knee")
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.toggleable(
                value = isExpanded,
                onValueChange = { isExpanded = it },
            ),
            text = "Pose detection settings",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h3
        )
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(MaterialTheme.colors.primary))
        Spacer(Modifier.height(25.dp))
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = isExpanded,
            enter = slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                for (joint in joints) {
                    this.item {
                        Label(joint.label)
                    }
                    this.item {
                        Switch(joint.check, joint.update)
                    }
                }
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = "compare $text angles",
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.body1
    )
}

@Composable
private fun Switch(checked: LiveData<Boolean>, updateValue: (Boolean) -> Unit) {
    Switch(
        checked = checked.observeAsState().value ?: true,
        onCheckedChange = updateValue,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colors.secondary,
            checkedTrackColor = MaterialTheme.colors.secondary,
        ),
    )
}
