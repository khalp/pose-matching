package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.textview.MaterialTextView
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.utils.Defines
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel
import java.text.DecimalFormat

class GameFinishedFragment2 : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_finished2, container, false)

        val scoreString = "${getString(R.string.congratulations)} ${DecimalFormat("#.#").format(gameViewModel.calculateOverallScore())}"
        view.findViewById<MaterialTextView>(R.id.congrats).text = scoreString

        initializeObservers(view)

        return view
    }

    private fun initializeObservers(view: View) {
        gameViewModel.gameState.observe(viewLifecycleOwner, { gameState ->
            if (gameState == Defines.GameState.PAUSED) {
                view.findNavController()
                    .navigate(GameFinishedFragment2Directions.actionGameFinishedFragment2ToGameLauncherFragment())
            }
        })
    }
}