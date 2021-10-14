package com.microsoft.device.display.samples.posematching.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.utils.CameraImageAnalyzer
import com.microsoft.device.display.samples.posematching.utils.GraphicOverlay
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.PoseViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel
import java.io.File
import java.text.DecimalFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    private val poseViewModel: PoseViewModel by viewModels()
    private val gameViewModel: GameViewModel by activityViewModels()
    private val referenceViewModel: ReferenceViewModel by activityViewModels()

    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var graphicOverlay: GraphicOverlay

    companion object {
        @JvmStatic
        fun newInstance() = CameraFragment()

        private const val TAG = "CameraXBasic"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    // Reference: https://stackoverflow.com/questions/66551781/android-onrequestpermissionsresult-is-deprecated-are-there-any-alternatives
    @androidx.camera.core.ExperimentalGetImage
    private val permissionsReq =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val granted = it.entries.all { permission ->
                permission.value == true
            }
            if (granted) {
                startCamera()
            } else {
                showMessage(R.string.permissions_not_granted)
                requireActivity().finish()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        // Find the the PreviewView and GraphicOverlay components
        previewView = view.findViewById(R.id.preview_view)
        graphicOverlay = view.findViewById(R.id.graphic_overlay)

        // Set up the listener for take photo button
        val textField = view.findViewById<TextView>(R.id.countdown_text)
        view.findViewById<Button>(R.id.camera_capture_button).setOnClickListener {
            object : CountDownTimer(3900, 1000) {

                // Count down every second (3, 2, 1) and then show "POSE" on 0
                override fun onTick(millisUntilFinished: Long) {
                    textField.textSize = 70f
                    textField.typeface = Typeface.DEFAULT_BOLD

                    val remainingSeconds = millisUntilFinished / 1000
                    textField.text = if (remainingSeconds > 0)
                        remainingSeconds.toString()
                    else
                        getString(R.string.pose)
                }

                // Clear text field and take snapshot
                override fun onFinish() {
                    textField.text = ""
                    takeVideoSnapshot()
                }
            }.start()
        }

        initializeObservers(view)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Add success/failure messages to viewmodel
        poseViewModel.displayStats = { stats ->
            Log.d(
                "Pose Comparator", stats.outputAngleDifferences(
                    { id: Int -> getString(id) },
                    { id: Int, sArg: String -> getString(id, sArg) },
                    { id: Int, sArg: String, fArg: Float -> getString(id, sArg, fArg) },
                )
            )
            val score = stats.calculateOverallScore()
            val msg = score?.let { DecimalFormat("#.##").format(it) } ?: getString(R.string.unable)
            showMessage(getString(R.string.score, msg))
        }

        return view
    }

    @androidx.camera.core.ExperimentalGetImage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CameraX reference: https://developer.android.com/codelabs/camerax-getting-started#0
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            permissionsReq.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun initializeObservers(view: View) {
        gameViewModel.isDualScreen.observe(viewLifecycleOwner, { isDualScreen ->
            if (!isDualScreen) {
                view.findNavController().navigate(CameraFragmentDirections.actionCameraFragmentToGameLauncherFragment())
            }
        })
    }

    private fun showMessage(@StringRes msgId: Int) {
        Toast.makeText(
            requireContext(),
            getString(msgId),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showMessage(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun takeVideoSnapshot() {
        poseViewModel.screenshot = true
    }

    @androidx.camera.core.ExperimentalGetImage
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            graphicOverlay.setImageSourceInfo(previewView.width, previewView.height, true)

            imageCapture = ImageCapture.Builder().build()

            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(previewView.height, previewView.width))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        CameraImageAnalyzer(
                            requireContext(),
                            graphicOverlay,
                            poseViewModel,
                            referenceViewModel
                        )
                    )
                }

            // Select front camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalysis
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
