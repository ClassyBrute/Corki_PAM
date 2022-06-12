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
import com.example.corki.viewmodel.AccountViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.DateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var texts = emptyList<TextView>()
    private var menus = emptyList<TextInputLayout>()

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.accountViewModel()
        observeAccountViewModel()

        binding.logoutButton.setOnClickListener {

            (activity as MainActivity).clearJWT()

            Toast.makeText(
                requireContext(),
                "User successfully logged out!",
                Toast.LENGTH_SHORT
            )
                .show()

            findNavController().navigate(R.id.action_navigation_profile_to_fragment_login)
            (activity as MainActivity).bottomNavGone()
        }

        with(binding) {
            texts = listOf(nameProfile, surnameProfile, cityProfile, phoneProfile, birthdayProfile)
            menus = listOf(
                nameProfileEdit, surnameProfileEdit, cityProfileEdit, phoneProfileEdit,
                birthdayProfileEdit
            )
        }

        // TODO check if profile owner == loggedIn user
        val loggedIn = true
        if (!loggedIn) binding.profileEdit.visibility = View.GONE

        binding.profileEdit.setOnClickListener {
            toggleEdit(true)

            with(binding) {
                // populate fields when editing
                nameProfileEdit1.setText(nameProfile.text)
                surnameProfileEdit1.setText(surnameProfile.text)
                cityProfileEdit1.setText(cityProfile.text)
                phoneProfileEdit1.setText(phoneProfile.text)
                birthdayProfileEdit1.setText(birthdayProfile.text)
                populateMenus()
            }
        }

        // TODO send edited fields to api
        binding.profileSave.setOnClickListener {
            toggleEdit(false)
        }

        // TODO don't send anything to api
        binding.profileCancel.setOnClickListener {
            toggleEdit(false)
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
                binding.birthdayProfileEdit1.setText(
                    DateFormat.getDateInstance().format(datePicker.selection)
                )
            }
        }

        return root
    }

    private fun toggleEdit(edit: Boolean) {
        when (edit) {
            true -> {
                menus.forEach { it.visibility = View.VISIBLE }
                texts.forEach { it.visibility = View.GONE }

                with(binding) {
                    profileSave.visibility = View.VISIBLE
                    profileCancel.visibility = View.VISIBLE
                    profileEdit.visibility = View.GONE
                }
            }
            false -> {
                menus.forEach { it.visibility = View.GONE }
                texts.forEach { it.visibility = View.VISIBLE }

                with(binding) {
                    profileSave.visibility = View.GONE
                    profileCancel.visibility = View.GONE
                    profileEdit.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun observeAccountViewModel() {
        accountViewModel.getProfile((activity as MainActivity).getJWT()).observe(viewLifecycleOwner) { data ->
            with(binding) {
                profileTitleTextView.text = "${data.firstName}'s profile"
                nameProfile.text = data.firstName
                surnameProfile.text = data.lastName
                cityProfile.text = data.address
                emailProfile.text = data.email //data.accountName
                phoneProfile.text = data.phoneNumber
                birthdayProfile.text = data.birthday
            }
        }
    }

    override fun onResume() {
        super.onResume()
        populateMenus()
    }

    private fun populateMenus() {
        val cities = resources.getStringArray(R.array.city)
        val arrayAdapter3 = ArrayAdapter(requireContext(), R.layout.search_item, cities)
        binding.cityProfileEdit1.setAdapter(arrayAdapter3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}