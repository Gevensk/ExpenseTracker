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
        val username = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()
        val firstName = binding.txtFirstName.text.toString()
        val lastName = binding.txtLastName.text.toString()
        val repeatPassword = binding.txtRePassword.text.toString()

        if (username.isEmpty()) {
            Toast.makeText(context, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if (firstName.isEmpty() || lastName.isEmpty()){
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }else if (password != repeatPassword) {
            Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
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