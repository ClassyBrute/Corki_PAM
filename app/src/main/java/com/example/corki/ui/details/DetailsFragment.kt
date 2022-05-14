package com.example.corki.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.corki.R
import com.example.corki.adapters.PostsAdapter
import com.example.corki.databinding.FragmentDetailsBinding
import com.example.corki.models.post.Post
import com.example.corki.viewmodel.PostViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.lang.NullPointerException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private var dateFrom = ""
    private var dateTo = ""
    private var dateLong = 0L

    //POST
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        postViewModel.postViewModel()
        observePostViewModel(arguments?.getString("id")!!)

        binding.detailsEdit.setOnClickListener {
            val texts: List<TextView>
            val menus: List<TextInputLayout>

            with (binding) {
                texts = listOf(titleCard, subjectDetails, cityDetails, priceDetails,
                    dateDetails, durationDetails)
                menus = listOf(titleCardEdit, subjectDetailsEdit, dateDetailsEdit,
                    cityDetailsEdit, levelDetailsEdit, priceDetailsEdit, durationDetailsEdit)
            }

            if (binding.subjectDetailsEdit.visibility == View.VISIBLE) {
                menus.forEach { it.visibility = View.GONE }
                texts.forEach { it.visibility = View.VISIBLE }
            } else {
                menus.forEach { it.visibility = View.VISIBLE }
                texts.forEach { it.visibility = View.GONE }
            }
        }

        binding.dateDetailsEdit1.setText(DateFormat.getDateInstance().format(Date()))

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

    private fun observePostViewModel(id: String) {
        postViewModel.getPost(id).observe(viewLifecycleOwner) { data ->
            with(binding) {
                var tempString = ""

                //TITLE
                titleCard.text = data.title

                //SUBJECTS
                data.subjects.forEach { tempString += "$it, "}
                subjectDetails.text = tempString
                tempString = ""

                //CITIES
                data.cities.forEach { tempString += "$it, "}
                cityDetails.text = tempString
                tempString = ""

                //LEVEL
                data.level.forEach { tempString += "$it, "}
                levelDetails.text = tempString
                tempString = ""

                //PRICE
                priceDetails.text = data.price.toString()

                //DATE
                dateDetails.text = data.dateFrom

                //DURATION
                var duration = 0L
                try {
                    val dateTo = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.sss'Z'").parse(data.dateTo).time
                    val dateFrom = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.sss'Z'").parse(data.dateFrom).time
                    duration = dateTo - dateFrom

                } catch (ex: NullPointerException) {
                    Log.e("error", ex.message.toString())
                }
                durationDetails.text = "${(duration / 60000)} min"

                //OWNER
                ownerDetails.text = data.ownerId
            }
        }
    }
}
