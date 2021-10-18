package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.microsoft.device.display.samples.posematching.R
import com.microsoft.device.display.samples.posematching.viewmodels.GameViewModel
import com.microsoft.device.display.samples.posematching.viewmodels.ReferenceViewModel

class GameLauncherFragment : Fragment() {

    private val gameViewModel: GameViewModel by activityViewModels()
    private val referenceViewModel: ReferenceViewModel by activityViewModels()

    private lateinit var welcomeText: TextView
    private lateinit var pointIcon: ImageView
    private lateinit var carryIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_launcher, container, false)

        welcomeText = view.findViewById(R.id.welcome_text2)
        pointIcon = view.findViewById(R.id.point_icon)
        carryIcon = view.findViewById(R.id.carry_icon)

        initializeButtons(view)
        initializeObservers()

        return view
    }

    private fun initializeButtons(view: View) {
        carryIcon.setOnClickListener {
            if (referenceViewModel.referenceImage.value != null) {
                gameViewModel.startGame()
                gameViewModel.setNumImages(referenceViewModel.referencesInList)
                // transition to next screen
                view.findNavController()
                    .navigate(GameLauncherFragmentDirections.actionGameLauncherFragmentToCameraFragment())
            }
        }
    }

    private fun initializeObservers() {
        referenceViewModel.referenceImage.observe(viewLifecycleOwner, { uri ->
            if (uri != null) {
                welcomeText.setText(R.string.reference_welcome_string2)
                pointIcon.visibility = View.GONE
                carryIcon.visibility = View.VISIBLE
            } else {
                welcomeText.setText(R.string.spanned_welcome_string2)
                pointIcon.visibility = View.VISIBLE
                carryIcon.visibility = View.GONE
            }
        })
    }
}