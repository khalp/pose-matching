package com.microsoft.device.display.samples.posematching.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.microsoft.device.display.samples.posematching.R

/**
 * A simple [Fragment] subclass.
 * Use the [ReferenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReferenceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reference, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReferenceFragment()
    }
}