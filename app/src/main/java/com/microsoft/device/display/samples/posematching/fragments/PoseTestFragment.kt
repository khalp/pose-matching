package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.viewmodels.PoseViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PoseTestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PoseTestFragment : Fragment() {

    val viewModel: PoseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reference, container, false)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = PoseTestFragment()
    }
}