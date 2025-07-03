package com.gracedev.expensetracker.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gracedev.expensetracker.MainActivity
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentReportBinding
import com.gracedev.expensetracker.databinding.FragmentSignInBinding
import com.gracedev.expensetracker.viewmodel.UserViewModel


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
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
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.btnSignUp.setOnClickListener {
            val action = SignInFragmentDirections.actionSignUp()
            Navigation.findNavController(it).navigate(action)
        }

        binding.btnSignIn.setOnClickListener {
            inputValid()
        }
    }

    private fun inputValid() {
        val username = binding.txtUsername.text.toString().trim()
        val password = binding.txtPassword.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(context, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.login(username, password)

        viewModel.userLD.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                sharedPref.edit()
                    .putString("username", user.username)
                    .putInt("uuid", user.uuid)
                    .putBoolean("isLogin", true)
                    .apply()

                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(context, "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    companion object {
//        @JvmStatic
//        fun newInstance() =
//            SignInFragment().apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//    }
}