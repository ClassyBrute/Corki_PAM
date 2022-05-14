package com.example.corki.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.corki.MainActivity
import com.example.corki.R
import com.example.corki.databinding.FragmentRegisterBinding
import com.example.corki.viewmodel.AccountViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private var registerMap = mutableMapOf<String, String>()
    private var JWT = ""
    private lateinit var accountViewModel: AccountViewModel
    private var birthdayLong: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.accountViewModel()
        observeAccountViewModel()

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.birthdayRegisterEdit1.hint = DateFormat.getDateInstance().format(Date())
        binding.birthdayRegisterEdit1.setOnClickListener { showDatePicker() }

        binding.buttonRegister.setOnClickListener {
            registerMap = mutableMapOf<String, String>()
            if(!binding.emailRegisterEdit1.text.isNullOrEmpty()) {
                registerMap["accountName"] = binding.emailRegisterEdit1.text.toString()
                registerMap["email"] = binding.emailRegisterEdit1.text.toString()
            }
            if(!binding.passwordRegisterEdit1.text.isNullOrEmpty()) {
                registerMap["password"] = binding.passwordRegisterEdit1.text.toString()
            }
            if(!binding.nameRegisterEdit1.text.isNullOrEmpty()) {
                registerMap["firstName"] = binding.nameRegisterEdit1.text.toString()
            }
            if(!binding.surnameRegisterEdit1.text.isNullOrEmpty()) {
                registerMap["lastName"] = binding.surnameRegisterEdit1.text.toString()
            }
            if(birthdayLong != null) {
               registerMap["birthday"] = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'").format(birthdayLong)
            }
            accountViewModel.postRegister(registerMap)
        }

        // TODO CHECK USER INPUTS


        return root
    }

    private fun observeAccountViewModel() {
        accountViewModel.postRegister(registerMap).observe(viewLifecycleOwner) { data ->
            JWT = data
            when(JWT.isEmpty()) {
                false -> {
                    findNavController().navigate(R.id.action_fragment_register_to_navigation_search)
                    (activity as MainActivity).bottomNavVisible()
                }
                else -> {
                    Toast.makeText(context, "Account could not be created.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDatePicker() {
        val datePicker: MaterialDatePicker<Long>?

        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select dates")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()
            )
            .setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
            .build()

        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {
            birthdayLong = it
            val date = DateFormat.getDateInstance().format(it)
            binding.birthdayRegisterEdit1.setText(date)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}