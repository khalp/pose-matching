package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.utils.Defines
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel
import java.text.DecimalFormat

class PauseFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val referenceViewModel: ReferenceViewModel by activityViewModels()

    private lateinit var quitButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pause, container, false)

        quitButton = view.findViewById(R.id.quit_button)
        val score = gameViewModel.calculateCurrentScore(referenceViewModel.referencesInList)
        view.findViewById<MaterialTextView>(R.id.score_value).text =
            DecimalFormat("#.#").format(score)

        initializeButtons()
        initializeObservers(view)

        return view
    }

    private fun initializeButtons() {
        quitButton.setOnClickListener {
            referenceViewModel.clearImages()
            gameViewModel.stopGame()
        }
    }

    private fun initializeObservers(view: View) {
        gameViewModel.gameState.observe(viewLifecycleOwner, { gameState ->
            if (gameState == Defines.GameState.RUNNING) {
                view.findNavController()
                    .navigate(PauseFragmentDirections.actionPauseFragmentToReferenceFragment())
            } else if (gameState == Defines.GameState.STOPPED) {
                view.findNavController()
                    .navigate(PauseFragmentDirections.actionPauseFragmentToWelcomeFragment1())
            }
        })
    }
}