package com.microsoft.device.display.samples.posematching.utils

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.microsoft.device.display.samples.posematching.viewmodels.PoseViewModel
import java.nio.ByteBuffer

@androidx.camera.core.ExperimentalGetImage
class CameraImageAnalyzer(val context: Context,
                          val graphicOverlay: GraphicOverlay,
                          val viewModel: PoseViewModel
    ) : ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {
        image.image?.let { mediaImage ->
            val img = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            if (viewModel.screenshot) {
                viewModel.initializeGraphicOverlay(context.resources, graphicOverlay, img, true)
                viewModel.analyzeImage(context.resources, graphicOverlay, img, image)
                viewModel.screenshot = false
            } else {
                image.close()
            }
        }
    }
}