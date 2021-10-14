package com.microsoft.device.display.samples.posematching.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import java.util.stream.IntStream.range

@ExperimentalFoundationApi
@Composable
fun TimerMenu(seconds: LiveData<Int>, updateSeconds: (Int) -> Unit, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.toggleable(
                value = isExpanded,
                onValueChange = { isExpanded = it },
            ),
            text = "Timer countdown settings",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h3
        )
        Spacer(Modifier.fillMaxWidth().height(3.dp).background(MaterialTheme.colors.primary))
        Spacer(Modifier.height(15.dp))
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = isExpanded,
            enter = slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "timer countdown length",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body1
                )
                Spacer(Modifier.width(50.dp))
                SecondsDropdown(seconds, updateSeconds)
            }
        }
    }
}

@Composable
private fun SecondsDropdown(seconds: LiveData<Int>, updateSeconds: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${seconds.observeAsState().value ?: 3}")
        Box(contentAlignment = Alignment.Center) {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "timer countdown length options"
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                for (time in range(3, 21)) {
                    DropdownMenuItem(onClick = { updateSeconds(time); expanded = false }) {
                        Text(text = "$time")
                    }
                }
            }
        }
    }
}