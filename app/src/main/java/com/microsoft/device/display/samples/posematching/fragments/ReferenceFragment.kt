package com.microsoft.device.display.samples.posematching.fragments

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.common.InputImage
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.ui.view.Settings
import com.microsoft.device.display.samples.posematching.utils.Defines
import com.microsoft.device.display.samples.posematching.utils.Defines.DEFAULT_REFERENCES_PER_GAME
import com.microsoft.device.display.samples.posematching.utils.GraphicOverlay
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.PoseViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ReferenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReferenceFragment : Fragment() {

    private val referenceViewModel: ReferenceViewModel by activityViewModels()
    private val gameViewModel: GameViewModel by activityViewModels()
    private val poseViewModel: PoseViewModel by viewModels()

    private lateinit var referenceImage: ImageView
    private lateinit var graphicOverlay: GraphicOverlay
    private lateinit var pickImageButton: MaterialButton
    private lateinit var defaultReferencesButton: MaterialButton
    private lateinit var referenceText: TextView

    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reference, container, false)

        referenceImage = view.findViewById(R.id.reference_image)
        graphicOverlay = view.findViewById(R.id.reference_graphic_overlay)
        pickImageButton = view.findViewById(R.id.pick_image_button)
        defaultReferencesButton = view.findViewById(R.id.default_references_button)
        referenceText = view.findViewById(R.id.reference_text)

        view.findViewById<ComposeView>(R.id.settings_drawer).setContent {
            Settings(
                referenceViewModel.timerLength,
                { referenceViewModel.setTimerLength(it) },
                referenceViewModel.checkElbows,
                { referenceViewModel.setCheckElbows(it) },
                referenceViewModel.checkShoulders,
                { referenceViewModel.setCheckShoulders(it) },
                referenceViewModel.checkHips,
                { referenceViewModel.setCheckHips(it) },
                referenceViewModel.checkKnees,
                { referenceViewModel.setCheckKnees(it) },
            )
        }

        initializeButtons()
        initializeObservers(view)

        return view
    }

    @ExperimentalFoundationApi
    private fun initializeButtons() {
        pickImageButton.setOnClickListener {
            openGallery()
        }

        defaultReferencesButton.setOnClickListener {
            referenceViewModel.clearImages()

            val referenceList = listOf(
                R.drawable.pose1, R.drawable.pose2, R.drawable.pose3, R.drawable.pose4,
                R.drawable.pose5, R.drawable.pose6, R.drawable.pose7, R.drawable.pose8,
                R.drawable.pose9, R.drawable.pose10
            )

            val uniqueNumbers = (referenceList.indices).shuffled().take(DEFAULT_REFERENCES_PER_GAME)
            for (index in uniqueNumbers) {
                referenceViewModel.pushImage(getUriFromDrawable(referenceList[index]))
            }
        }
    }

    @ExperimentalFoundationApi
    private fun initializeObservers(view: View) {
        referenceViewModel.referenceImage.observe(viewLifecycleOwner, { uri ->
            if (uri != null) {
                referenceImage.setImageURI(uri)

                val img = InputImage.fromFilePath(requireContext(), uri)
                poseViewModel.initializeGraphicOverlay(graphicOverlay, img, false)
                poseViewModel.analyzeImage(graphicOverlay, img)

                pickImageButton.setText(R.string.add_reference_image)
            } else {
                pickImageButton.setText(R.string.pick_reference_image)
            }
            val text = if (referenceViewModel.referencesInList == 0) {
                ""
            } else {
                getString(R.string.number_reference_images) + " ${referenceViewModel.referencesInList}"
            }
            referenceText.text = text
        })

        // Game can only supports dual screen mode, pause game or quit if it is switched to single screen
        gameViewModel.isDualScreen.observe(viewLifecycleOwner, { isDualScreen ->
            if (!isDualScreen) {
                if (referenceViewModel.referencesInList == 0) {
                    gameViewModel.stopGame()
                    view.findNavController()
                        .navigate(ReferenceFragmentDirections.actionReferenceFragmentToWelcomeFragment1())
                } else {
                    gameViewModel.pauseGame()
                    view.findNavController()
                        .navigate(ReferenceFragmentDirections.actionReferenceFragmentToPauseFragment())
                }
            }
        })

        gameViewModel.gameState.observe(viewLifecycleOwner, { gameState ->
            if (gameState == Defines.GameState.FINISHED) {
                view.findNavController()
                    .navigate(ReferenceFragmentDirections.actionReferenceFragmentToGameFinishedFragment1())
            }
            if (gameState == Defines.GameState.RUNNING) {
                pickImageButton.visibility = View.INVISIBLE
                defaultReferencesButton.visibility = View.INVISIBLE
            } else {
                pickImageButton.visibility = View.VISIBLE
                defaultReferencesButton.visibility = View.VISIBLE
            }
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
                    referenceViewModel.pushImage(imageUri)
                }
            }
        }

    private fun getUriFromDrawable(id: Int): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://${resources.getResourcePackageName(id)}"
                    + "/${resources.getResourceTypeName(id)}"
                    + "/${resources.getResourceEntryName(id)}"
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReferenceFragment()
    }
}