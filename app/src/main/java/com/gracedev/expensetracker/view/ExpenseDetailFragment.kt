package com.gracedev.expensetracker.view

import android.content.Context
import android.content.SharedPreferences
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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ExpenseDetailFragment : DialogFragment() {
    private lateinit var binding: FragmentExpenseDetailBinding
    private var expense: Expense? = null
    private lateinit var viewModel: DetailExpenseViewModel
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

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

        sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("uuid", -1)  // -1 sebagai default jika tidak ditemukan

        viewModel = ViewModelProvider(this)[DetailExpenseViewModel::class.java]
        val timestamp = expense?.date ?: 0
        val dateFormatted = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("in", "ID"))
            .format(Date(timestamp * 1000L))


        binding.txtDetail.text = expense?.name
        binding.txtDate.text = dateFormatted
        binding.txtNominal.text = "IDR ${formatter.format(expense?.nominal)}"

        viewModel.getBudgetNameById(expense?.budgetId ?:0, userId).observe(viewLifecycleOwner) { name ->
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
