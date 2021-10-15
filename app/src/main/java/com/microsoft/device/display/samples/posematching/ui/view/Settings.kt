package com.microsoft.device.display.samples.posematching.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.microsoft.device.display.samples.posematching.ui.theme.PoseMatchingTheme
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun Settings(
    seconds: LiveData<Int>,
    updateSeconds: (Int) -> Unit,
    checkElbows: LiveData<Boolean>,
    updateElbows: (Boolean) -> Unit,
    checkShoulders: LiveData<Boolean>,
    updateShoulders: (Boolean) -> Unit,
    checkKnees: LiveData<Boolean>,
    updateKnees: (Boolean) -> Unit,
    checkHips: LiveData<Boolean>,
    updateHips: (Boolean) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    PoseMatchingTheme {
        ModalDrawer(
            modifier = Modifier.fillMaxSize(),
            drawerState = drawerState,
            drawerContent = {
                Spacer(Modifier.height(15.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Settings",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h2
                )
                Spacer(Modifier.height(30.dp))
                AccessibilityMenu(
                    checkElbows,
                    updateElbows,
                    checkShoulders,
                    updateShoulders,
                    checkHips,
                    updateHips,
                    checkKnees,
                    updateKnees,
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 30.dp),
                )
                Spacer(Modifier.height(30.dp))
                TimerMenu(
                    seconds,
                    updateSeconds,
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 30.dp)
                )
            },
            content = {
                IconButton(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp)
                        .background(MaterialTheme.colors.surface, RoundedCornerShape(percent = 15)),
                    onClick = { scope.launch { drawerState.open() } },
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "open game settings",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        )
    }
}