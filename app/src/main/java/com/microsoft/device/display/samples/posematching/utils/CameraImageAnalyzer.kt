package com.microsoft.device.display.samples.posematching.utils

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.microsoft.device.display.samples.posematching.viewmodels.PoseViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel
import java.nio.ByteBuffer

@androidx.camera.core.ExperimentalGetImage
class CameraImageAnalyzer(
    val context: Context,
    val graphicOverlay: GraphicOverlay,
    private val viewModel: PoseViewModel,
    private val referenceViewModel: ReferenceViewModel,
) : ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { mediaImage ->
            if (viewModel.screenshot) {
                val refUri = referenceViewModel.imageUri.value
                refUri?.let { uri ->
                    val img =
                        InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    val refImg = InputImage.fromFilePath(context, uri)
                    viewModel.initializeGraphicOverlay(graphicOverlay, img, true)
                    viewModel.resetPoses()
                    viewModel.analyzeImage(
                        graphicOverlay = graphicOverlay,
                        referenceImage = refImg,
                        image = img,
                        imageProxy = imageProxy
                    )
                    viewModel.screenshot = false
                }
            } else {
                imageProxy.close()
            }
        }
    }
}