package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel

class GameFinishedFragment1 : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val referenceViewModel: ReferenceViewModel by activityViewModels()

    private lateinit var restartGameButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_finished1, container, false)

        restartGameButton = view.findViewById(R.id.restart_game_button)

        initializeButtons(view)
        initializeObservers(view)

        return view
    }

    private fun initializeButtons(view: View) {
        restartGameButton.setOnClickListener {
            referenceViewModel.clearImages()
            gameViewModel.pauseGame()
            gameViewModel.clearScore()
            view.findNavController()
                .navigate(GameFinishedFragment1Directions.actionGameFinishedFragment1ToWelcomeFragment1())
        }
    }

    private fun initializeObservers(view: View) {
    }
}