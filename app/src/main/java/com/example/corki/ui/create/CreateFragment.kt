package com.example.corki.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.text.parseAsHtml
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.corki.R
import com.example.corki.databinding.FragmentCreateBinding
import com.example.corki.models.post.Post
import com.example.corki.viewmodel.PostViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.json.JSONObject
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private var dateFrom = ""
    private var dateTo = ""
    private var dateLong = 0L

    private lateinit var postViewModel: PostViewModel
    private var postsList = emptyList<Post>()

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
            json.put("dateFrom", dateFrom)
        }
        if (!binding.durationDetailsEdit1.text.isNullOrEmpty()) {
            val tempDate = dateLong + binding.durationDetailsEdit1.text.toString().toInt() * 60000
            dateTo = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'").format(tempDate)
            json.put("dateTo", dateTo)
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

        val duration = resources.getStringArray(R.array.duration)
        val arrayAdapter4 = ArrayAdapter(requireContext(), R.layout.search_item, duration)
        binding.durationDetailsEdit1.setAdapter(arrayAdapter4)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}