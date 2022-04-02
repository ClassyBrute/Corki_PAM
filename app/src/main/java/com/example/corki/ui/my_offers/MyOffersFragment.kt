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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val myOffersViewModel =
                ViewModelProvider(this).get(MyOffersViewModel::class.java)

        _binding = FragmentMyOffersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMyOffers
        myOffersViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}