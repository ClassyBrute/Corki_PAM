package com.example.corki.ui.details

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.corki.databinding.FragmentDetailsBinding
import com.example.corki.ui.create.CreateViewModel

class DetailsFragment: Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.titleCard.text = arguments?.getString("title")

        binding.detailsEdit.setOnClickListener {
            val edits: List<EditText>
            val texts: List<TextView>

            with (binding) {
                edits = listOf(titleCardEdit, subjectDetailsEdit,
                    cityDetailsEdit, priceDetailsEdit, dateDetailsEdit)
                texts = listOf(titleCard, subjectDetails, cityDetails, priceDetails, dateDetails)
            }

            if (binding.subjectDetailsEdit.visibility == View.VISIBLE) {
                edits.forEach { it.visibility = View.GONE }
                texts.forEach { it.visibility = View.VISIBLE }
            } else {
                edits.forEach { it.visibility = View.VISIBLE }
                texts.forEach { it.visibility = View.GONE }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
