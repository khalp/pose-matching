package com.microsoft.device.display.samples.posematching.viewmodels

import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.microsoft.device.display.samples.posematching.utils.GraphicOverlay
import com.microsoft.device.display.samples.posematching.utils.PoseGraphic
import com.microsoft.device.display.samples.posematching.utils.comparePoses

class PoseViewModel : ViewModel() {
    var screenshot = false
    private val poseDetector: PoseDetector
    private var referencePose: Pose? = null
    private var userPose: Pose? = null
    private var skipElbows: Boolean = false
    private var skipShoulders: Boolean = false
    private var skipKnees: Boolean = false
    private var skipHips: Boolean = false
    var onSuccess: () -> Unit = {}
    var onFail: () -> Unit = {}

    init {
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()

        poseDetector = PoseDetection.getClient(options)
    }

    fun initializeGraphicOverlay(
        graphicOverlay: GraphicOverlay,
        image: InputImage,
        isImageFlipped: Boolean = false,
    ) {
        graphicOverlay.setImageSourceInfo(image.width, image.height, isFlipped = isImageFlipped)
    }

    fun resetPoses() {
        referencePose = null
        userPose = null
    }

    fun analyzeImage(
        graphicOverlay: GraphicOverlay,
        referenceImage: InputImage,
        image: InputImage,
        imageProxy: ImageProxy? = null,
    ) {
        // Process reference image to get reference pose data
        poseDetector.process(referenceImage)
            .addOnSuccessListener {
                referencePose = it
                drawAndComparePoses(graphicOverlay)
            }

        // Process user image to get image pose data
        poseDetector.process(image)
            .addOnSuccessListener {
                userPose = it
                imageProxy?.close()
                drawAndComparePoses(graphicOverlay)
            }
            .addOnFailureListener { e ->
                Log.d("PoseTest", "Boo, failure", e)
                imageProxy?.close()
            }
    }

    private fun drawAndComparePoses(graphicOverlay: GraphicOverlay) {
        // REVISIT: not very async friendly, haven't figured out how to "join" the tasks yet
        if (referencePose == null || userPose == null)
            return

        drawPoses(graphicOverlay, userPose!!)
        if (comparePoses(
                skipElbows,
                skipShoulders,
                skipHips,
                skipKnees,
                referencePose!!,
                userPose!!
            )
        ) {
            onMatchSuccess()
        } else {
            onMatchFail()
        }
    }

    private fun drawPoses(graphicOverlay: GraphicOverlay, pose: Pose) {
        graphicOverlay.clear()

        graphicOverlay.add(
            PoseGraphic(
                graphicOverlay,
                pose,
                showInFrameLikelihood = false,
                visualizeZ = true,
                rescaleZForVisualization = false,
                poseClassification = emptyList()
            )
        )

        graphicOverlay.invalidate()
    }

    private fun onMatchSuccess() {
        Log.d("PoseTest", "Poses match!")
        onSuccess()
    }

    private fun onMatchFail() {
        Log.d("PoseTest", "Poses don't match, try again :(")
        onFail()
    }
}

