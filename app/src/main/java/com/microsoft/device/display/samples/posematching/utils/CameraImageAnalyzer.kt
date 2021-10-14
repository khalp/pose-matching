package com.microsoft.device.display.samples.posematching.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { mediaImage ->
            if (viewModel.screenshot) {
                // Load the reference image and check if it's flipped
                val refUri = referenceViewModel.referenceImage.value ?: return
                var inputStream = context.contentResolver.openInputStream(refUri) ?: return
                val isFlippedHorizontally =
                    ExifInterface(inputStream).getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    ) == ExifInterface.ORIENTATION_FLIP_HORIZONTAL
                inputStream.close()

                inputStream = context.contentResolver.openInputStream(refUri) ?: return
                var bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                val matrix = Matrix()
                matrix.postScale(-1f, 1f)
                if (isFlippedHorizontally) {
                    bitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        matrix,
                        false,
                    )
                }

                // Create InputImages for reference + user photos
                val refImg = InputImage.fromBitmap(bitmap, 0)
                val img =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                // Draw skeleton over user and analyze the pose matching
                viewModel.initializeGraphicOverlay(graphicOverlay, img, true)
                viewModel.resetPoses()
                viewModel.analyzeAndCompareImages(
                    graphicOverlay = graphicOverlay,
                    referenceImage = refImg,
                    image = img,
                    imageProxy = imageProxy
                )
                viewModel.screenshot = false
            } else {
                imageProxy.close()
            }
        }
    }
}