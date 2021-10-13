package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel

class WelcomeFragment2 : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private val referenceViewModel: ReferenceViewModel by activityViewModels()

    private lateinit var welcomeText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome2, container, false)

        welcomeText = view.findViewById(R.id.welcome_text2)

        initializeObservers()

        return view
    }

    private fun initializeObservers() {
        viewModel.gameStarted.observe(viewLifecycleOwner, { started ->
            if (started) {
                welcomeText.setText(R.string.reference_welcome_string2)
            }
        })

        viewModel.isDualScreen.observe(viewLifecycleOwner, { isDualScreen ->
            if (isDualScreen) {
                welcomeText.setText(R.string.spanned_welcome_string2)
            }
        })

        referenceViewModel.imageUri.observe(viewLifecycleOwner, { uri ->
            if (uri != null) {
                // transition to next screen
                requireView().findNavController().navigate(WelcomeFragment2Directions.actionWelcomeFragment2ToCameraFragment())
            }
        })
    }
}