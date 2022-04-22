package com.example.corki.ui.profile

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
import com.example.corki.databinding.FragmentProfileBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.DateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.profileEdit.setOnClickListener {
            val texts: List<TextView>
            val menus: List<TextInputLayout>

            with (binding) {
                texts = listOf(nameProfile, surnameProfile, cityProfile, phoneProfile, birthdayProfile)
                menus = listOf(nameProfileEdit, surnameProfileEdit, cityProfileEdit,
                    phoneProfileEdit, birthdayProfileEdit)
            }

            if (binding.nameProfileEdit.visibility == View.VISIBLE) {
                menus.forEach { it.visibility = View.GONE }
                texts.forEach { it.visibility = View.VISIBLE }
            } else {
                menus.forEach { it.visibility = View.VISIBLE }
                texts.forEach { it.visibility = View.GONE }
            }
        }

        var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>?
        var dateRange: Pair<Long, Long>?
        binding.birthdayProfileEdit1.setText(DateFormat.getDateInstance().format(Date()))

        binding.birthdayProfileEdit1.setOnClickListener {
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
                binding.birthdayProfileEdit1.setText(date)
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        val cities = resources.getStringArray(R.array.city)
        val arrayAdapter3 = ArrayAdapter(requireContext(), R.layout.search_item, cities)
        binding.cityProfileEdit1.setAdapter(arrayAdapter3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}