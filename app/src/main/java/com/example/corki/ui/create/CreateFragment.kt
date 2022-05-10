package com.example.corki.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.corki.R
import com.example.corki.databinding.FragmentCreateBinding
import com.example.corki.ui.search.ItemsViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private val data = ArrayList<ItemsViewModel>()
    private var firstJson: String = ""
    private var secondJson: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val createViewModel = ViewModelProvider(this).get(CreateViewModel::class.java)

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.createButton.setOnClickListener { createOffer() }

        binding.dateDetailsEdit1.hint = DateFormat.getDateInstance().format(Date())
        binding.dateDetailsEdit1.setOnClickListener { showDatePicker() }

        return root
    }

    private fun showDatePicker() {
        val dateRangePicker: MaterialDatePicker<Pair<Long, Long>>?
        var dateRange: Pair<Long, Long>?

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

        dateRangePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

        dateRangePicker.addOnPositiveButtonClickListener {
            //TODO TIME PICKER MAYBE
            dateRange = Pair(dateRangePicker.selection?.first, dateRangePicker.selection?.second)
            val first = DateFormat.getDateInstance().format(dateRange?.first)
            firstJson = "${SimpleDateFormat("yyyy-MM-dd").format(Date(dateRange!!.first))}T00:00:00Z"
            val second = DateFormat.getDateInstance().format(dateRange?.second)
            secondJson = "${SimpleDateFormat("yyyy-MM-dd").format(Date(dateRange!!.second))}T23:59:59Z"

            val date = "$first - $second"
            binding.dateDetailsEdit1.setText(date)
        }
    }

    private fun createOffer() {
        val json = JSONObject()

        if (!binding.titleCardEdit1.text.isNullOrEmpty()) {
            json.put("title", binding.titleCardEdit1.text)
        }
        if (!binding.subjectDetailsEdit1.text.isNullOrEmpty()) {
            json.put("subject", binding.subjectDetailsEdit1.text)
        }
        if (!binding.cityDetailsEdit1.text.isNullOrEmpty()) {
            json.put("city", binding.cityDetailsEdit1.text)
        }
        if (!binding.levelDetailsEdit1.text.isNullOrEmpty()) {
            json.put("level", binding.levelDetailsEdit1.text)
        }
        if (!binding.priceDetailsEdit1.text.isNullOrEmpty()) {
            json.put("price", binding.priceDetailsEdit1.text)
        }
        if (!binding.dateDetailsEdit1.text.isNullOrEmpty()) {
            json.put("dateFrom", firstJson)
            json.put("dateTo", secondJson)
        }

        Toast.makeText(context, json.toString(), Toast.LENGTH_LONG).show()
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