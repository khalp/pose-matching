package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.microsoft.device.display.samples.posematching.MainActivity
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.WelcomeViewModel

class WelcomeFragment2 : Fragment() {

    private val viewModel: WelcomeViewModel by activityViewModels()
    private val referenceViewModel: ReferenceViewModel by activityViewModels()

    private lateinit var welcomeText: TextView

    private var isAppSpanned = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome2, container, false)

        welcomeText = view.findViewById(R.id.welcome_text2)

        initializeObservers()

        (requireActivity() as? MainActivity)?.let { isAppSpanned = it.isSpanned }

        if (isAppSpanned) {
            welcomeText.setText(R.string.spanned_welcome_string2)
        }

        return view
    }

    private fun initializeObservers() {
        viewModel.gameStarted.observe(viewLifecycleOwner, { started ->
            if (started) {
                welcomeText.setText(R.string.reference_welcome_string2)
            }
        })

        referenceViewModel.imageUri.observe(viewLifecycleOwner, { uri ->
            if (uri != null) {
                // transition to next screen
                parentFragmentManager.beginTransaction()
                    .replace(R.id.secondary_fragment_container, CameraFragment.newInstance())
                    .commit()
            }
        })
    }
}