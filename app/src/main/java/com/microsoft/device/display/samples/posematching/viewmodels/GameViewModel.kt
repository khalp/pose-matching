package com.microsoft.device.display.samples.posematching.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.microsoft.device.display.samples.posematching.utils.Defines

class GameViewModel : ViewModel() {
    private val _gameState = MutableLiveData(Defines.GameState.STOPPED)
    val gameState: LiveData<Defines.GameState>
        get() = _gameState

    val isDualScreen = MutableLiveData(false)

    fun startGame() {
        _gameState.value = Defines.GameState.RUNNING
    }

    fun pauseGame() {
        _gameState.value = Defines.GameState.PAUSED
    }

    fun finishGame() {
        _gameState.value = Defines.GameState.STOPPED
    }
}