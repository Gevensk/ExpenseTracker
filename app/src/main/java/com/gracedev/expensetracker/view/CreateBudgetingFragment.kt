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
import com.gracedev.expensetracker.databinding.FragmentCreateBudgetingBinding
import com.gracedev.expensetracker.model.Budget
import com.gracedev.expensetracker.viewmodel.DetailBudgetViewModel


class CreateBudgetingFragment : Fragment() {
    private lateinit var binding:FragmentCreateBudgetingBinding
    private lateinit var viewModel:DetailBudgetViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailBudgetViewModel::class.java)
        binding.btnAdd.setOnClickListener {
            var todo = Budget(
                binding.txtNama.text.toString(),
                binding.txtNominal.text.toString()
            )
            val list = listOf(todo)
            viewModel.addTodo(list)
            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()
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