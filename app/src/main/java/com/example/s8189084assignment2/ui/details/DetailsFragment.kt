package com.example.s8189084assignment2.ui.details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.s8189084assignment2.R
import com.google.android.material.appbar.MaterialToolbar

// Shows every field for one sport, using the Sport object passed in via Safe Args.
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sport = args.sport

        // Get references to every view this Fragment needs to update.
        val toolbar = view.findViewById<MaterialToolbar>(R.id.detailsToolbar)
        val sportNameText = view.findViewById<TextView>(R.id.sportNameText)
        val playerCountValueText = view.findViewById<TextView>(R.id.playerCountValueText)
        val fieldTypeValueText = view.findViewById<TextView>(R.id.fieldTypeValueText)
        val olympicDot = view.findViewById<View>(R.id.olympicDot)
        val olympicStatusValueText = view.findViewById<TextView>(R.id.olympicStatusValueText)
        val descriptionValueText = view.findViewById<TextView>(R.id.descriptionValueText)

        // The back arrow returns to Dashboard, same as the system back button.
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        sportNameText.text = sport.sportName
        playerCountValueText.text = sport.playerCount.toString()
        fieldTypeValueText.text = sport.fieldType
        descriptionValueText.text = sport.description

        // Swap the dot color and Yes/No text depending on the Olympic status.
        if (sport.olympicSport) {
            olympicDot.setBackgroundResource(R.drawable.shape_dot_blue)
            olympicStatusValueText.text = getString(R.string.details_olympic_yes)
        } else {
            olympicDot.setBackgroundResource(R.drawable.shape_dot_grey)
            olympicStatusValueText.text = getString(R.string.details_olympic_no)
        }
    }
}
