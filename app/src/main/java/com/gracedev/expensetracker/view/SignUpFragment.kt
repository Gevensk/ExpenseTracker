package com.gracedev.expensetracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentSignUpBinding
import com.gracedev.expensetracker.model.User
import com.gracedev.expensetracker.viewmodel.UserViewModel

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnBack.setOnClickListener{
            val action = SignUpFragmentDirections.actionSignIn()
            Navigation.findNavController(it).navigate(action)
        }

        binding.btnCreate.setOnClickListener {
            if (inputValid() == true) {
                val username = binding.txtUsername.text.toString()
                val password = binding.txtPassword.text.toString()
                val firstName = binding.txtFirstName.text.toString()
                val lastName = binding.txtLastName.text.toString()

                val user = User(username, firstName, lastName, password)

                viewModel.checkUsernameExists(username)

                viewModel.usernameExistsLD.observe(viewLifecycleOwner) { ada ->
                    if (ada) {
                        Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.register(user)
                    }
                }
            }
        }
        viewModel.registrationSuccessLD.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                val action = SignUpFragmentDirections.actionSignIn()
                Navigation.findNavController(requireView()).navigate(action)
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inputValid(): Boolean {
        val username = binding.txtUsername.text.toString().trim()
        val password = binding.txtPassword.text.toString().trim()
        val repeatPassword = binding.txtRePassword.text.toString().trim()
        val firstName = binding.txtFirstName.text.toString().trim()
        val lastName = binding.txtLastName.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(context, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (firstName.isEmpty()) {
            Toast.makeText(context, "First name tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastName.isEmpty()) {
            Toast.makeText(context, "Last name tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (repeatPassword.isEmpty()) {
            Toast.makeText(context, "Ulangi password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != repeatPassword) {
            Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}