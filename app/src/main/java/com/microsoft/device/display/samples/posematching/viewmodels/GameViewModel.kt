package com.microsoft.device.display.samples.posematching.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _gameStarted = MutableLiveData(false)
    val gameStarted: LiveData<Boolean>
        get() = _gameStarted

    val isDualScreen = MutableLiveData(false)

    fun startGame() {
        _gameStarted.value = true
    }

    fun startGameReceived() {
        _gameStarted.value = false
    }
}