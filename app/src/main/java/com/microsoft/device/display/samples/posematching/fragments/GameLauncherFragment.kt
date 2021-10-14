package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.utils.Defines
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel

class GameLauncherFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val referenceViewModel: ReferenceViewModel by activityViewModels()

    private lateinit var welcomeText: TextView
    private lateinit var launchButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_launcher, container, false)

        welcomeText = view.findViewById(R.id.welcome_text2)
        launchButton = view.findViewById(R.id.launch_game_button)

        initializeButtons(view)
        initializeObservers()

        return view
    }

    private fun initializeButtons(view: View) {
        launchButton.setOnClickListener {
            if (referenceViewModel.imageUri.value == null) {
                gameViewModel.startGame()
            } else {
                // transition to next screen
                view.findNavController()
                    .navigate(GameLauncherFragmentDirections.actionGameLauncherFragmentToCameraFragment())
            }
        }
    }

    private fun initializeObservers() {
        gameViewModel.gameStarted.observe(viewLifecycleOwner, { gameState ->
            if (gameState == Defines.GameState.STOPPED) {
                welcomeText.setText(R.string.spanned_welcome_string2)
                launchButton.setText(R.string.start_game)
                launchButton.visibility = View.VISIBLE
            } else {
                welcomeText.setText(R.string.reference_welcome_string2)
                launchButton.visibility = View.GONE
            }
        })

        referenceViewModel.imageUri.observe(viewLifecycleOwner, { uri ->
            if (uri != null) {
                launchButton.setText(R.string.launch_game)
                launchButton.visibility = View.VISIBLE
            }
        })
    }
}