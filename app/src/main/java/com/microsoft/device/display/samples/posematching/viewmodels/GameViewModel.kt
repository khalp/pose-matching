package com.microsoft.device.display.samples.posematching.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.microsoft.device.display.samples.posematching.utils.Defines

class GameViewModel : ViewModel() {
    private val _score = MutableLiveData(0f)
    val score: LiveData<Float>
        get() = _score

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

    fun stopGame() {
        _gameState.value = Defines.GameState.STOPPED
    }

    fun finishGame() {
        _gameState.value = Defines.GameState.FINISHED
    }

    fun clearScore() {
        _score.value = 0f
    }

    fun addScore(newScore: Float) {
        _score.value = (_score.value ?: 0f) + newScore
    }
}