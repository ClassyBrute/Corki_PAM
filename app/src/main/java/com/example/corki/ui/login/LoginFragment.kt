package com.example.corki.ui.login

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.corki.MainActivity
import com.example.corki.databinding.FragmentLoginBinding

import com.example.corki.R
import com.example.corki.viewmodel.AccountViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var loginMap = mutableMapOf<String, String>()
    private var JWT = ""
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.accountViewModel()
        observeAccountViewModel()

        binding.login.setOnClickListener {
            loginMap = mutableMapOf<String, String>()
            loginMap["accountName"] = binding.username.text.toString()
            loginMap["password"] = binding.password.text.toString()
            accountViewModel.postLogin(loginMap)

            //binding.loading.visibility = View.VISIBLE
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_login_to_fragment_register)
        }
    }

    private fun observeAccountViewModel() {
        accountViewModel.postLogin(loginMap).observe(viewLifecycleOwner) { data ->
            JWT = data
            when(JWT.isEmpty()) {
                false -> {
                    (activity as MainActivity).putJWT(JWT)
                    findNavController().navigate(R.id.action_fragment_login_to_navigation_search)
                    (activity as MainActivity).bottomNavVisible()
                }
                else -> {
                    Toast.makeText(context, "Incorrect login or password.", Toast.LENGTH_LONG).show()
                }
            }
        }
//        accountViewModel.getTokenError().observe(viewLifecycleOwner) { data ->
//            tokenNotFound = data
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}