package com.microsoft.device.display.samples.posematching.viewmodels

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.microsoft.device.display.samples.posematching.R

class PoseViewModel : ViewModel() {
    private val poseDetector: PoseDetector

    init {
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()

        poseDetector = PoseDetection.getClient(options)
    }

    fun analyzeImage(resources: Resources) {
        val rotationDegrees = 0
        val image = InputImage.fromBitmap(BitmapFactory.decodeResource(resources, R.drawable.bill),
            rotationDegrees)

        val result = poseDetector.process(image)
            .addOnSuccessListener { results ->
                // Task completed successfully
                // ...
                for(landmark in results.allPoseLandmarks) {
                    Log.d("PoseTest", "Confidence ${landmark.inFrameLikelihood}, Position ${landmark.position}" +
                            ", Type ${landmark.landmarkType}")
                }

                Log.d("PoseTest", "Success!")
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Log.d("PoseTest", "Boo, failure")
            }
    }
}