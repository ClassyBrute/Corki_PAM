package com.example.corki.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corki.R
import com.example.corki.databinding.FragmentSearchBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.util.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var _recyclerView: RecyclerView? = null
    private val recyclerView get() = _recyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _recyclerView = binding.recyclerSearch
        recyclerView?.layoutManager = LinearLayoutManager(this.context)

        var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>?
        var dateRange: Pair<Long, Long>?
        binding.dateTime1.setText(DateFormat.getDateInstance().format(Date()))

        binding.dateTime1.setOnClickListener {
            dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                .build()

            dateRangePicker!!.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

            dateRangePicker?.addOnPositiveButtonClickListener {
                dateRange = Pair(dateRangePicker!!.selection?.first, dateRangePicker!!.selection?.second)
                val first = DateFormat.getDateInstance().format(dateRange?.first)
                val second = DateFormat.getDateInstance().format(dateRange?.second)
                val date = "$first - $second"
                binding.dateTime1.setText(date)
            }
        }

        val data = ArrayList<ItemsViewModel>()
        data.add(ItemsViewModel("hejka"))
        data.add(ItemsViewModel("hejka123"))
        data.add(ItemsViewModel("hejka123456"))
        data.add(ItemsViewModel("hejka456"))
        data.add(ItemsViewModel("hejka456"))
        data.add(ItemsViewModel("hejka456"))

        val adapter = ItemAdapter(data)
        recyclerView?.adapter = adapter

        return root
    }

    override fun onResume() {
        super.onResume()
        val subjects = resources.getStringArray(R.array.subjects)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.search_item, subjects)
        binding.menuSubjectItem.setAdapter(arrayAdapter1)

        val levels = resources.getStringArray(R.array.level)
        val arrayAdapter2 = ArrayAdapter(requireContext(), R.layout.search_item, levels)
        binding.menuLevelItem.setAdapter(arrayAdapter2)

        val cities = resources.getStringArray(R.array.city)
        val arrayAdapter3 = ArrayAdapter(requireContext(), R.layout.search_item, cities)
        binding.menuCityItem.setAdapter(arrayAdapter3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}