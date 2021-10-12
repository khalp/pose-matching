package com.microsoft.device.display.samples.posematching.viewmodels

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.utils.GraphicOverlay
import com.microsoft.device.display.samples.posematching.utils.PoseGraphic
import java.util.*

class PoseViewModel : ViewModel() {
    private val poseDetector: PoseDetector

    init {
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()

        poseDetector = PoseDetection.getClient(options)
    }

    fun initializeGraphicOverlay(
        resources: Resources,
        graphicOverlay: GraphicOverlay,
        image: InputImage = InputImage.fromBitmap(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.bill
            ), 0
        ),
        isImageFlipped: Boolean = false,
    ) {
        graphicOverlay.setImageSourceInfo(image.width, image.height, isFlipped = isImageFlipped)
    }

    fun analyzeImage(resources: Resources, graphicOverlay: GraphicOverlay, image: InputImage = InputImage.fromBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.bill),
        0
    )) {
        poseDetector.process(image)
            .addOnSuccessListener { results ->
                // Task completed successfully
                // ...
                graphicOverlay.clear()
                graphicOverlay.add(
                    PoseGraphic(
                        graphicOverlay,
                        results,
                        showInFrameLikelihood = false,
                        visualizeZ = true,
                        rescaleZForVisualization = false,
                        poseClassification = ArrayList()
                    )
                )

                graphicOverlay.invalidate()

                for (landmark in results.allPoseLandmarks) {
                    Log.d(
                        "PoseTest",
                        "Confidence ${landmark.inFrameLikelihood}, Position ${landmark.position}" +
                                ", Type ${landmark.landmarkType}"
                    )
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

