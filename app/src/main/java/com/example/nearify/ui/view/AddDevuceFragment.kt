package com.example.nearify.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.ComposeView
import com.example.nearify.R


class AddDevuceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val cont = inflater.inflate(R.layout.composite_container, container, false)
        val composableContainer = cont.findViewById<LinearLayout>(R.id.compose_container)
        // Create a new ComposeView and ensure it's not already attached to a parent
        val composableView = ComposeView(requireContext()).apply {
            setContent {

            }
        }


        // Add the ComposeView to the container
        composableContainer.addView(composableView)

        return cont
    }
}



