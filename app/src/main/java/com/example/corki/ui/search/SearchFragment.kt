package com.example.corki.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corki.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var _recyclerView: RecyclerView? = null
    private val recyclerView get() = _recyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        val searchViewModel =
                ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _recyclerView = binding.recyclerSearch
        recyclerView?.layoutManager = LinearLayoutManager(this.context)

        val data = ArrayList<ItemsViewModel>()
        data.add(ItemsViewModel("hejka"))
        data.add(ItemsViewModel("hejka123"))
        data.add(ItemsViewModel("hejka123456"))
        data.add(ItemsViewModel("hejka456"))

        val adapter = ItemAdapter(data, object: ItemAdapter.OnDataClickListener {
            override fun onDataClick(position: Int) {
                println(position)
            }
        })

        recyclerView?.adapter = adapter

//        val textView: TextView = binding
//        searchViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}