package com.example.corki.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.corki.MainActivity
import com.example.corki.R
import com.example.corki.databinding.FragmentCreateBinding
import com.example.corki.viewmodel.PostViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private var dateFrom = ""
    private var dateTo = ""
    private var dateLong = 0L

    private lateinit var postViewModel: PostViewModel
    private var map = mutableMapOf<String, String>()
    private var canBeSent = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.createButton.setOnClickListener { createOffer() }

        binding.dateDetailsEdit1.hint = DateFormat.getDateInstance().format(Date())
        binding.dateDetailsEdit1.setOnClickListener { showDatePicker() }

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        postViewModel.postViewModel()
        observePostViewModel()

        return root
    }

    private fun observePostViewModel() {
        postViewModel.postIdGetter().observe(viewLifecycleOwner) { data ->
            if(data.isNotEmpty()) {
                //val bundle = bundleOf("id" to data)
                //findNavController().navigate(R.id.action_fragment_create_to_fragment_details, bundle)
                Toast.makeText(context, "Post created!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Post could not be created!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createOffer() {
        map = mutableMapOf<String, String>()
        canBeSent = true

        if (!binding.titleCardEdit1.text.isNullOrEmpty()) {
            map["title"] = binding.titleCardEdit1.text.toString()
        } else { canBeSent = false }

        if (!binding.subjectDetailsEdit1.text.isNullOrEmpty()) {
            map["subjects"] = binding.subjectDetailsEdit1.text.toString().lowercase()
        } else { canBeSent = false }

        if (!binding.cityDetailsEdit1.text.isNullOrEmpty()) {
            map["cities"] = binding.cityDetailsEdit1.text.toString()
        } else { canBeSent = false }

        if (!binding.levelDetailsEdit1.text.isNullOrEmpty()) {
            map["level"] = binding.levelDetailsEdit1.text.toString().lowercase()
        } else { canBeSent = false }

        if (!binding.priceDetailsEdit1.text.isNullOrEmpty()) {
            map["price"] = binding.priceDetailsEdit1.text.toString()
        } else { canBeSent = false }

        if (!binding.dateDetailsEdit1.text.isNullOrEmpty()) {
            map["dateFrom"] = dateFrom
        } else { canBeSent = false }

        if (!binding.durationDetailsEdit1.text.isNullOrEmpty()) {
            val tempDate = dateLong + binding.durationDetailsEdit1.text.toString().toInt() * 60000
            dateTo = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'").format(tempDate)
            map["dateTo"] = dateTo
        } else { canBeSent = false }

        if(canBeSent) {
            postViewModel.postPost(map, (activity as MainActivity).getJWT())
        } else {
            Toast.makeText(context, "You're missing something!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val datePicker: MaterialDatePicker<Long> = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
            .build()

        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select time")
                .build()

            timePicker.show(parentFragmentManager, "MATERIAL_TIME_PICKER")

            timePicker.addOnPositiveButtonClickListener {
                var hour = timePicker.hour.toString()
                var minute = timePicker.minute.toString()
                val date = DateFormat.getDateInstance().format(datePicker.selection)

                if (hour.length < 2) hour = "0$hour"
                if (minute.length < 2) minute = "0$minute"

                dateFrom = "${SimpleDateFormat("yyyy-MM-dd").format(datePicker.selection)}T$hour:$minute:00Z"
                dateLong = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'").parse(dateFrom).time

                binding.dateDetailsEdit1.setText("$date $hour:$minute")
            }
        }
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

        val duration = resources.getStringArray(R.array.duration)
        val arrayAdapter4 = ArrayAdapter(requireContext(), R.layout.search_item, duration)
        binding.durationDetailsEdit1.setAdapter(arrayAdapter4)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}