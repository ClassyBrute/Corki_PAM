package com.example.corki.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.textDetails.text = arguments?.getString("title")
        binding.textDetails1.text = arguments?.getString("title1")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
