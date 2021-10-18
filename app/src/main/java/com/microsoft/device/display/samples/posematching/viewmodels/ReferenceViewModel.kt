package com.microsoft.device.display.samples.posematching.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReferenceViewModel : ViewModel() {
    private val _images = mutableListOf<Uri>()
    private val _image = MutableLiveData<Uri?>()
    val referenceImage: LiveData<Uri?>
        get() = _image

    val referencesInList: Int
        get() = _images.size

    // update the visible imageUri with the top of the uri list
    private fun peekImage() {
        _image.value = if (_images.isNotEmpty()) _images[0] else null
    }

    // advance to the next imageUri
    fun popImage() {
        if (_images.isNotEmpty()) {
            _images.removeAt(0)
            peekImage()
        }
    }

    // add another imageUri to the list
    fun pushImage(uri: Uri) {
        _images.add(uri)
        peekImage()
    }

    // hide the visible imageUri (used for pausing the game)
    fun hideImage() {
        _image.value = null
    }

    fun clearImages() {
        _images.clear()
        peekImage()
    }

    private val _checkElbows = MutableLiveData<Boolean>()
    val checkElbows: LiveData<Boolean>
        get() = _checkElbows

    fun setCheckElbows(value: Boolean) {
        _checkElbows.value = value
    }

    private val _checkShoulders = MutableLiveData<Boolean>()
    val checkShoulders: LiveData<Boolean>
        get() = _checkShoulders

    fun setCheckShoulders(value: Boolean) {
        _checkShoulders.value = value
    }

    private val _checkHips = MutableLiveData<Boolean>()
    val checkHips: LiveData<Boolean>
        get() = _checkHips

    fun setCheckHips(value: Boolean) {
        _checkHips.value = value
    }

    private val _checkKnees = MutableLiveData<Boolean>()
    val checkKnees: LiveData<Boolean>
        get() = _checkKnees

    fun setCheckKnees(value: Boolean) {
        _checkKnees.value = value
    }

    private val _timerLength = MutableLiveData<Int>()
    val timerLength: LiveData<Int>
        get() = _timerLength

    fun setTimerLength(value: Int) {
        _timerLength.value = value
    }
}