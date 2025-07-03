package com.gracedev.expensetracker.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentCreateBudgetingBinding
import com.gracedev.expensetracker.model.Budget
import com.gracedev.expensetracker.viewmodel.DetailBudgetViewModel


class CreateBudgetingFragment : Fragment() {
    private lateinit var binding:FragmentCreateBudgetingBinding
    private lateinit var viewModel:DetailBudgetViewModel

    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("uuid", -1)

        viewModel = ViewModelProvider(this).get(DetailBudgetViewModel::class.java)
        binding.btnAdd.setOnClickListener {
            val nama = binding.txtNama.text.toString()
            val nominalStr = binding.txtNominal.text.toString()

            if (nama.isEmpty()) {
                Toast.makeText(view.context, "Nama harus diisi", Toast.LENGTH_SHORT).show()
            }
            else if (nominalStr.isEmpty()) {
                Toast.makeText(view.context, "Nominal harus diisi", Toast.LENGTH_SHORT).show()
            }
            else {
                val nominal = nominalStr.toIntOrNull()
                if (nominal == null || nominal <= 0) {
                    Toast.makeText(view.context, "Nominal harus angka positif", Toast.LENGTH_SHORT).show()
                }
                else {
                    val budget = Budget(nama, nominal, userId)
                    val list = listOf(budget)
                    viewModel.addTodo(list)
                    Toast.makeText(view.context, "Data added", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(it).popBackStack()
                }
            }
//            var budget = Budget(
//                binding.txtNama.text.toString(),
//                binding.txtNominal.text.toString().toInt()
//            )
//            val list = listOf(budget)
//            viewModel.addTodo(list)
//            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
//            Navigation.findNavController(it).popBackStack()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBudgetingBinding.inflate(inflater,container,false)
        return binding.root

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateBudgetingFragment().apply {

            }
    }
}