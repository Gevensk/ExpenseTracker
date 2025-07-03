package com.gracedev.expensetracker.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentReportBinding
import com.gracedev.expensetracker.viewmodel.ListBudgetViewModel
import com.gracedev.expensetracker.viewmodel.ListExpenseViewModel
import java.text.NumberFormat
import java.util.Locale


class ReportFragment : Fragment() {
    private lateinit var binding: FragmentReportBinding
    private lateinit var budgetViewModel: ListBudgetViewModel
    private lateinit var expenseViewModel: ListExpenseViewModel

    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("uuid", -1)  // -1 sebagai default jika tidak ditemukan

        budgetViewModel = ViewModelProvider(this).get(ListBudgetViewModel::class.java)
        expenseViewModel = ViewModelProvider(this).get(ListExpenseViewModel::class.java)

        budgetViewModel.refresh(userId)
        expenseViewModel.refresh(userId)

        budgetViewModel.budgetLD.observe(viewLifecycleOwner) { budgets ->
            expenseViewModel.expenseLD.observe(viewLifecycleOwner) { expenses ->

                val usedBudgetMap = budgets.associate { budget ->
                    val totalExpense = expenses
                        .filter { it.budgetId == budget.uuid }
                        .sumOf { it.nominal }
                    budget.uuid to totalExpense
                }

                // Pasang adapter RecyclerView
                val adapter = ReportListAdapter(budgets, usedBudgetMap)
                binding.recViewReport.adapter = adapter
                binding.recViewReport.layoutManager = LinearLayoutManager(requireContext())

                // Hitung total
                val totalUsed = usedBudgetMap.values.sum()
                val totalBudget = budgets.sumOf { it.budget }

                val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))
                binding.txtExpenseBudget.text =
                    "IDR ${formatter.format(totalUsed)} / IDR ${formatter.format(totalBudget)}"
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        binding.recViewReport.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {

            }
    }
}