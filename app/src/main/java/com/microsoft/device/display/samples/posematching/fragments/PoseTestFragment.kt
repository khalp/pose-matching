package com.microsoft.device.display.samples.posematching.fragments

//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.viewModels
//import com.google.android.material.button.MaterialButton
//import com.microsoft.device.display.samples.posematching.R
//import com.microsoft.device.display.samples.posematching.utils.GraphicOverlay
//import com.microsoft.device.display.samples.posematching.viewmodels.PoseViewModel
//
///**
// * A simple [Fragment] subclass.
// * Use the [PoseTestFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class PoseTestFragment : Fragment() {
//
//    private val viewModel: PoseViewModel by viewModels()
//
//    private lateinit var generateBtn: MaterialButton
//    private lateinit var graphicOverlay: GraphicOverlay
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_pose_test, container, false)
//
//        generateBtn = view.findViewById(R.id.generate_button)
//        generateBtn.setOnClickListener {
//            //generatePose()
//        }
//
//        graphicOverlay = view.findViewById(R.id.graphic_overlay)
//        graphicOverlay.background = ContextCompat.getDrawable(requireContext(), R.drawable.bill)
//        viewModel.initializeGraphicOverlay(resources, graphicOverlay)
//
//        return view
//    }
//
//    /**
//     * Analyze an image and generate a pose estimation
//     */
//    private fun generatePose() {
//        viewModel.analyzeImage(resources, graphicOverlay)
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance() = PoseTestFragment()
//    }
//}