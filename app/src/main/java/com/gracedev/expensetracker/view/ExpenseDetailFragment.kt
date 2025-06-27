package com.gracedev.expensetracker.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentExpenseDetailBinding
import com.gracedev.expensetracker.model.Expense
import com.gracedev.expensetracker.viewmodel.DetailExpenseViewModel
import com.gracedev.expensetracker.viewmodel.ListBudgetViewModel
import com.gracedev.expensetracker.viewmodel.ListExpenseViewModel


class ExpenseDetailFragment : DialogFragment() {
    private lateinit var binding: FragmentExpenseDetailBinding
    private var expense: Expense? = null
    private lateinit var viewModel: DetailExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            expense = it.getParcelable(ARG_EXPENSE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailExpenseViewModel::class.java]
        binding.txtDetail.text = expense?.name
        binding.txtDate.text = expense?.date
        binding.txtNominal.text = expense?.nominal?.toString()

        viewModel.getBudgetNameById(expense?.budgetId ?:0).observe(viewLifecycleOwner) { name ->
            binding.chipBudget.text = name
        }
    }


    companion object {
        private val ARG_EXPENSE = "expense"

        @JvmStatic
        fun newInstance(expense: Expense) =
            ExpenseDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EXPENSE, expense)
                }
            }
    }
}
