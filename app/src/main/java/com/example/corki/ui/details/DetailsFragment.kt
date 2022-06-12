package com.example.corki.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.corki.MainActivity
import com.example.corki.R
import com.example.corki.databinding.FragmentDetailsBinding
import com.example.corki.viewmodel.AccountViewModel
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
    private var dateLong: Long? = 0L

    private val format = "yyyy-MM-dd'T'kk:mm:ss.sss'Z'"

    private var texts = emptyList<TextView>()
    private var menus = emptyList<TextInputLayout>()

    //POST
    private lateinit var postViewModel: PostViewModel

    //ACCOUNT
    private lateinit var accountViewModel: AccountViewModel
    private var ownerId = ""
    private var currentUser = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        postViewModel.postViewModel()
        observePostViewModel(arguments?.getString("id")!!)

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.accountViewModel()

        observeAccountViewModelId()
        observeAccountViewModelToken()

        with(binding) {
            texts = listOf(
                titleCard, subjectDetails, cityDetails, priceDetails,
                dateDetails, durationDetails
            )
            menus = listOf(
                titleCardEdit, subjectDetailsEdit, dateDetailsEdit,
                cityDetailsEdit, levelDetailsEdit, priceDetailsEdit, durationDetailsEdit
            )
        }

        binding.detailsEdit.setOnClickListener {
            toggleEdit(true)

            with(binding) {
                // populate fields when editing
                titleCardEdit1.setText(titleCard.text)
                subjectDetailsEdit1.setText(subjectDetails.text)
                cityDetailsEdit1.setText(cityDetails.text)
                levelDetailsEdit1.setText(levelDetails.text)
                priceDetailsEdit1.setText(priceDetails.text.dropLast(2))
                durationDetailsEdit1.setText(durationDetails.text.dropLast(3))
                dateDetailsEdit1.setText(dateDetails.text)
                populateMenus()
            }
        }

        // TODO send edited fields to api
        binding.detailsSave.setOnClickListener {
            toggleEdit(false)
        }

        // TODO don't send anything to api
        binding.detailsCancel.setOnClickListener {
            toggleEdit(false)
        }

        // TODO register and deregister
        binding.detailsRegister.setOnClickListener {

        }

        binding.dateDetailsEdit1.setText(DateFormat.getDateInstance().format(Date()))

        binding.dateDetailsEdit1.setOnClickListener { showDatePicker() }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return root
    }

    private fun toggleEdit(edit: Boolean) {
        when (edit) {
            true -> {
                menus.forEach { it.visibility = View.VISIBLE }
                texts.forEach { it.visibility = View.GONE }

                with(binding) {
                    detailsSave.visibility = View.VISIBLE
                    detailsCancel.visibility = View.VISIBLE
                    detailsEdit.visibility = View.GONE
                }
            }
            false -> {
                menus.forEach { it.visibility = View.GONE }
                texts.forEach { it.visibility = View.VISIBLE }

                with(binding) {
                    detailsSave.visibility = View.GONE
                    detailsCancel.visibility = View.GONE
                    detailsEdit.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
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

                dateFrom =
                    "${SimpleDateFormat("yyyy-MM-dd").format(datePicker.selection)}T$hour:$minute:00Z"
                dateLong = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'").parse(dateFrom)?.time

                binding.dateDetailsEdit1.setText("$date $hour:$minute")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        populateMenus()
    }

    private fun populateMenus() {
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

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun observePostViewModel(id: String) {
        postViewModel.getPost(id).observe(viewLifecycleOwner) { data ->
            ownerId = data.ownerId
            accountViewModel.getProfile((activity as MainActivity).getJWT())
            accountViewModel.getAccount(ownerId)

            with(binding) {
                titleCard.text = data.title
                subjectDetails.text = data.subjects[0].replaceFirst(
                    oldChar = data.subjects[0][0],
                    newChar = data.subjects[0][0].uppercase().toCharArray()[0]
                )
                cityDetails.text = data.cities[0]
                levelDetails.text = when (data.level[0][0]) {
                    'p' -> "Primary School"
                    'm' -> "Middle School"
                    'h' -> "High School"
                    'u' -> "University"
                    else -> {
                        "unknown"
                    }
                }

                priceDetails.text = "${data.price}zł"

                dateDetails.text = SimpleDateFormat(format).parse(data.dateFrom)?.let {
                    DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(it)
                }

                try {
                    val dateTo = SimpleDateFormat(format).parse(data.dateTo)?.time
                    val dateFrom = SimpleDateFormat(format).parse(data.dateFrom)?.time
                    val duration = dateFrom?.let { dateTo?.minus(it) }

                    if (duration != null) durationDetails.text = "${duration / 60000} min"
                } catch (ex: NullPointerException) {
                    durationDetails.text = context?.getString(R.string.invalid_time)
                }
            }
        }
    }

    private fun observeAccountViewModelId() {
        accountViewModel.accountByIdGetter().observe(viewLifecycleOwner) { data ->
            binding.ownerDetails.text = "${data.firstName} ${data.lastName}"
        }
    }

    private fun observeAccountViewModelToken() {
        accountViewModel.accountByTokenGetter().observe(viewLifecycleOwner) { data ->
            currentUser = data.id
            if (ownerId == currentUser) {
                binding.detailsEdit.visibility = View.VISIBLE
                binding.detailsRegister.visibility = View.GONE
            }
            else {
                binding.detailsEdit.visibility = View.GONE
                binding.detailsRegister.visibility = View.VISIBLE
            }
        }
    }
}
