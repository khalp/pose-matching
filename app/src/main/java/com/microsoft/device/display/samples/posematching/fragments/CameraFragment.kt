package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.microsoft.device.display.samples.posematching.R
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult

class CameraFragment : Fragment() {
    private lateinit var cameraView: CameraView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    // CameraView reference: https://natario1.github.io/CameraView/about/getting-started
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraView = requireActivity().findViewById(R.id.camera)
        cameraView.setLifecycleOwner(viewLifecycleOwner)
        cameraView.addCameraListener(PoseMatchingCameraListener())
    }

    override fun onStart() {
        super.onStart()
        cameraView.open()
    }

    override fun onResume() {
        super.onResume()
        cameraView.open()
    }

    override fun onPause() {
        super.onPause()
        cameraView.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CameraFragment()
    }
}

private class PoseMatchingCameraListener: CameraListener() {
    override fun onPictureTaken(result: PictureResult) {
        // A Picture was taken!
    }

    override fun onVideoTaken(result: VideoResult) {
        // A Video was taken!
    }
}