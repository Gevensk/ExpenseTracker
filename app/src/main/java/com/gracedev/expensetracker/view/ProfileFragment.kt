package com.gracedev.expensetracker.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gracedev.expensetracker.SignActivity
import com.gracedev.expensetracker.databinding.FragmentProfileBinding
import com.gracedev.expensetracker.viewmodel.UserViewModel


class ProfileFragment : Fragment(), EditPasswordListener {
    private lateinit var binding: FragmentProfileBinding
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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.oldPassword = viewModel.oldPassword
        binding.newPassword = viewModel.newPassword
        binding.repeatPassword = viewModel.repeatPassword

        binding.listener = this

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.passwordChangeResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                // Misal: Beri Toast sukses
                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                // Bersihkan input
                viewModel.oldPassword.value = ""
                viewModel.newPassword.value = ""
                viewModel.repeatPassword.value = ""
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })

//        binding.btnSignOut.setOnClickListener {
//            val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
//            sharedPref.edit().clear().apply()
//
//            val intent = Intent(requireActivity(), SignActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//
//        binding.btnChange.setOnClickListener{
//            val oldPassword = binding.txtOldPassword.text.toString()
//            val newPassword = binding.txtNewPassword.text.toString()
//            val rePassword = binding.txtRePassword.text.toString()
//        }
    }

    fun observeViewModel() {
        viewModel.userLD.observe(viewLifecycleOwner, Observer {
            binding.user = it
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSignOutClick(v: View) {
        val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        val intent = Intent(requireActivity(), SignActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onChangePasswordClick(v: View) {
        viewModel.changePassword()
    }
}