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

    val isReferenceListEmpty: Boolean
        get() = _images.isEmpty()

    // update the visible imageUri with the top of the uri list
    fun peekImage() {
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
    }

    // hide the visible imageUri (used for pausing the game)
    fun hideImage() {
        _image.value = null
    }

    fun clearImages() {
        _images.clear()
        peekImage()
    }
}