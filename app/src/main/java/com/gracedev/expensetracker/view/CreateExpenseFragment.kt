package com.gracedev.expensetracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentCreateExpenseBinding
import com.gracedev.expensetracker.viewmodel.ListBudgetViewModel


class CreateExpenseFragment : Fragment() {
    private lateinit var binding: FragmentCreateExpenseBinding
    private lateinit var viewModel: ListBudgetViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListBudgetViewModel::class.java)
        viewModel.refresh()

        viewModel.budgetLD.observe(viewLifecycleOwner, Observer { budgets ->
            val spinnerItems = budgets.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateExpenseFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}