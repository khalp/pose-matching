package com.microsoft.device.display.samples.posematching.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReferenceViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri>
        get() = _imageUri

    fun setImageUri(uri: Uri) { _imageUri.value = uri }
}