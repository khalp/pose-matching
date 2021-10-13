package com.microsoft.device.display.samples.posematching.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.common.InputImage
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.utils.GraphicOverlay
import com.microsoft.device.display.samples.posematching.viewmodels.PoseViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ReferenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReferenceFragment : Fragment() {

    private val viewModel: ReferenceViewModel by activityViewModels()
    private val poseViewModel: PoseViewModel by viewModels()

    private lateinit var referenceImage: ImageView
    private lateinit var graphicOverlay: GraphicOverlay
    private lateinit var pickImageButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reference, container, false)

        referenceImage = view.findViewById(R.id.reference_image)
        graphicOverlay = view.findViewById(R.id.reference_graphic_overlay)
        pickImageButton = view.findViewById(R.id.pick_image_button)

        pickImageButton.setOnClickListener {
            openGallery()
        }

        initializeObservers()

        return view
    }

    private fun initializeObservers() {
        viewModel.imageUri.observe(viewLifecycleOwner, { uri ->
            referenceImage.setImageURI(uri)

            val img = InputImage.fromFilePath(requireContext(), uri)
            poseViewModel.initializeGraphicOverlay(resources, graphicOverlay, img, false)
            poseViewModel.analyzeImage(resources, graphicOverlay, img)
        })
    }

    /**
     * Send an intent for the user to pick an image from the device's gallery
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        getResult.launch(intent)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { imageUri ->
                    viewModel.setImageUri(imageUri)
                }
            }
        }

    companion object {
        @JvmStatic
        fun newInstance() = ReferenceFragment()
    }
}