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
import com.microsoft.device.display.samples.posematching.utils.Defines
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel

class WelcomeFragment1 : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    private lateinit var welcomeText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome1, container, false)

        welcomeText = view.findViewById(R.id.welcome_text1)

        initializeObservers(view)

        return view
    }

    private fun initializeObservers(view: View) {
        viewModel.gameState.observe(viewLifecycleOwner, { gameState ->
            if (gameState != Defines.GameState.STOPPED) {
                // transition to next screen
                view.findNavController().navigate(WelcomeFragment1Directions.actionWelcomeFragment1ToReferenceFragment())
            }
        })

        viewModel.isDualScreen.observe(viewLifecycleOwner, { isDualScreen ->
            if (isDualScreen) {
                welcomeText.setText(R.string.spanned_welcome_string1)
            } else {
                welcomeText.setText(R.string.welcome_string1)
            }
        })
    }
}