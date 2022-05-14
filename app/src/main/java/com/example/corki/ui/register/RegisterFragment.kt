package com.example.corki.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.Pair
import androidx.navigation.fragment.findNavController
import com.example.corki.MainActivity
import com.example.corki.R
import com.example.corki.databinding.FragmentRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.birthdayRegisterEdit1.hint = DateFormat.getDateInstance().format(Date())
        binding.birthdayRegisterEdit1.setOnClickListener { showDatePicker() }

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_register_to_navigation_search)
            (activity as MainActivity).bottomNavVisible()
        }

        // TODO CHECK USER INPUTS


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
//            firstJson = "${SimpleDateFormat("yyyy-MM-dd").format(Date(dateRange!!.first))}T00:00:00Z"
            val second = DateFormat.getDateInstance().format(dateRange?.second)
//            secondJson = "${SimpleDateFormat("yyyy-MM-dd").format(Date(dateRange!!.second))}T23:59:59Z"

            val date = "$first - $second"
            binding.birthdayRegisterEdit1.setText(date)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}