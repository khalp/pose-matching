package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.microsoft.device.display.samples.posematching.MainActivity
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.viewmodels.WelcomeViewModel

class WelcomeFragment1 : Fragment() {

    private val viewModel: WelcomeViewModel by activityViewModels()

    private lateinit var welcomeText: TextView
    private lateinit var startButton: MaterialButton

    private var isAppSpanned = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome1, container, false)

        welcomeText = view.findViewById(R.id.welcome_text1)
        startButton = view.findViewById(R.id.start_button1)

        initializeObservers()

        (requireActivity() as? MainActivity)?.let { isAppSpanned = it.isSpanned }

        if (isAppSpanned) {
            welcomeText.setText(R.string.spanned_welcome_string1)
            initializeButton(startButton)
        }

        return view
    }

    private fun initializeButton(button: MaterialButton) {
        startButton.visibility = View.VISIBLE
        button.setOnClickListener {
            viewModel.startGame()
        }
    }

    private fun initializeObservers() {
        viewModel.gameStarted.observe(viewLifecycleOwner, { started ->
            if (started) {
                // transition to next screen
                parentFragmentManager.beginTransaction()
                    .replace(R.id.primary_fragment_container, ReferenceFragment.newInstance())
                    .commit()
            }
        })
    }
}