package com.example.corki.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.corki.MainActivity
import com.example.corki.databinding.FragmentLoginBinding

import com.example.corki.R
import com.example.corki.viewmodel.AccountViewModel

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
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
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.accountViewModel()
        observeAccountViewModel()

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                binding.login.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    binding.username.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    binding.password.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                binding.loading.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }
        }
        binding.username.addTextChangedListener(afterTextChangedListener)
        binding.password.addTextChangedListener(afterTextChangedListener)
        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }
            false
        }

        binding.login.setOnClickListener {
            loginMap = mutableMapOf<String, String>()
            loginMap["accountName"] = binding.username.text.toString()
            loginMap["password"] = binding.password.text.toString()
            accountViewModel.postLogin(loginMap)

            binding.loading.visibility = View.VISIBLE
            loginViewModel.login(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
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

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}