package com.example.corki.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.corki.R
import com.example.corki.databinding.FragmentCreateBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.util.*

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val createViewModel = ViewModelProvider(this).get(CreateViewModel::class.java)

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // date picker
        var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>?
        var dateRange: Pair<Long, Long>?
        binding.dateDetailsEdit1.setText(DateFormat.getDateInstance().format(Date()))

        binding.dateDetailsEdit1.setOnClickListener {
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
                binding.dateDetailsEdit1.setText(date)
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        val subjects = resources.getStringArray(R.array.subjects)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.search_item, subjects)
        binding.subjectDetailsEdit1.setAdapter(arrayAdapter1)

        val levels = resources.getStringArray(R.array.level)
        val arrayAdapter2 = ArrayAdapter(requireContext(), R.layout.search_item, levels)
        binding.levelDetailsEdit1.setAdapter(arrayAdapter2)

        val cities = resources.getStringArray(R.array.city)
        val arrayAdapter3 = ArrayAdapter(requireContext(), R.layout.search_item, cities)
        binding.cityDetailsEdit1.setAdapter(arrayAdapter3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}