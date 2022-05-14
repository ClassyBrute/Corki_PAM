package com.example.corki.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.corki.MainActivity
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

        binding.logoutButton.setOnClickListener {
            // TODO logout user
            Toast.makeText(
                requireContext(),
                "User successfully logged out!",
                Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_navigation_profile_to_fragment_login)
            (activity as MainActivity).bottomNavGone()
        }

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

        binding.birthdayProfileEdit1.setText(DateFormat.getDateInstance().format(Date()))

        binding.birthdayProfileEdit1.setOnClickListener {
            val datePicker: MaterialDatePicker<Long> = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select dates")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                .build()

            datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {
                binding.birthdayProfileEdit1.setText(DateFormat.getDateInstance().format(datePicker.selection))
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