package com.microsoft.device.display.samples.posematching.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReferenceViewModel : ViewModel() {
    private val _imageUris = mutableListOf<Uri>()
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?>
        get() = _imageUri

    val isUriListEmpty: Boolean
        get() = _imageUris.isEmpty()

    // update the visible imageUri with the top of the uri list
    fun peekImageUri() {
        _imageUri.value = if (_imageUris.isNotEmpty()) _imageUris[0] else null
    }

    // advance to the next imageUri
    fun popImageUri() {
        if (_imageUris.isNotEmpty()) {
            _imageUris.removeAt(0)
            peekImageUri()
        }
    }

    // add another imageUri to the list
    fun pushImageUri(uri: Uri) {
        _imageUris.add(uri)
    }

    // hide the visible imageUri (used for pausing the game)
    fun hideImageUri() {
        _imageUri.value = null
    }

    fun clearImageUris() {
        _imageUris.clear()
        peekImageUri()
    }
}