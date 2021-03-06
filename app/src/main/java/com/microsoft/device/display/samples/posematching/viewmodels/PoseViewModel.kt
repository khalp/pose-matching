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
import com.microsoft.device.display.samples.posematching.utils.MatchingStats
import com.microsoft.device.display.samples.posematching.utils.PoseGraphic
import com.microsoft.device.display.samples.posematching.utils.comparePoses

class PoseViewModel : ViewModel() {
    var screenshot = false
    private val poseDetector: PoseDetector
    private var referencePose: Pose? = null
    private var userPose: Pose? = null
    var displayStats: (MatchingStats) -> Unit = {}

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
        image: InputImage,
    ) {
        poseDetector.process(image)
            .addOnSuccessListener {
                userPose = it
                drawPoses(graphicOverlay, it)
            }
    }

    // REVISIT: reference image is automatically analyzed/calculated now, no need to do it twice
    fun analyzeAndCompareImages(
        graphicOverlay: GraphicOverlay,
        referenceImage: InputImage,
        image: InputImage,
        imageProxy: ImageProxy? = null,
        referenceViewModel: ReferenceViewModel,
    ) {
        // Process reference image to get reference pose data
        poseDetector.process(referenceImage)
            .addOnSuccessListener {
                referencePose = it
                drawAndComparePoses(graphicOverlay, referenceViewModel)
            }
            .addOnFailureListener { e ->
                Log.d("PoseViewModel", "Failed to process reference image", e)
            }

        // Process user image to get image pose data
        poseDetector.process(image)
            .addOnSuccessListener {
                userPose = it
                imageProxy?.close()
                drawAndComparePoses(graphicOverlay, referenceViewModel)
            }
            .addOnFailureListener { e ->
                Log.d("PoseViewModel", "Failed to process user image", e)
                imageProxy?.close()
            }
    }

    private fun drawAndComparePoses(
        graphicOverlay: GraphicOverlay,
        referenceViewModel: ReferenceViewModel
    ) {
        // REVISIT: not very async friendly, haven't figured out how to "join" the tasks yet
        if (referencePose == null || userPose == null)
            return

        drawPoses(graphicOverlay, userPose!!)
        val checkElbows = referenceViewModel.checkElbows.value ?: true
        val checkShoulders = referenceViewModel.checkShoulders.value ?: true
        val checkHips = referenceViewModel.checkHips.value ?: true
        val checkKnees = referenceViewModel.checkKnees.value ?: true

        val stats = comparePoses(
            !checkElbows,
            !checkShoulders,
            !checkHips,
            !checkKnees,
            referencePose!!,
            userPose!!
        )
        displayStats(stats)
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
}

