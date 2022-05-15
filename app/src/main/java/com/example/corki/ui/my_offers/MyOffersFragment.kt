package com.example.corki.ui.my_offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.corki.databinding.FragmentMyOffersBinding

class MyOffersFragment : Fragment() {

    private var _binding: FragmentMyOffersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyOffersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textMyOffers.text = "This is MyOffers Fragment"
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}