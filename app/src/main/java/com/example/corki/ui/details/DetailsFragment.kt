package com.example.corki.ui.details

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
import com.example.corki.adapters.PostsAdapter
import com.example.corki.databinding.FragmentDetailsBinding
import com.example.corki.models.post.Post
import com.example.corki.viewmodel.PostViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.DateFormat
import java.util.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

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
                texts = listOf(titleCard, subjectDetails, cityDetails, priceDetails, dateDetails)
                menus = listOf(titleCardEdit, subjectDetailsEdit, dateDetailsEdit,
                    cityDetailsEdit, levelDetailsEdit, priceDetailsEdit)
            }

            if (binding.subjectDetailsEdit.visibility == View.VISIBLE) {
                menus.forEach { it.visibility = View.GONE }
                texts.forEach { it.visibility = View.VISIBLE }
            } else {
                menus.forEach { it.visibility = View.VISIBLE }
                texts.forEach { it.visibility = View.GONE }
            }
        }

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
                dateDetails.text = "${data.dateFrom} - ${data.dateTo}"

                //OWNER
                ownerDetails.text = data.ownerId
            }
        }
    }
}
