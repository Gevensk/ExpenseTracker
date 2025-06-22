package com.gracedev.expensetracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentExpenseListTrackerBinding
import com.gracedev.expensetracker.viewmodel.ListBudgetViewModel
import com.gracedev.expensetracker.viewmodel.ListExpenseViewModel


class ExpenseListTrackerFragment : Fragment() {
    private lateinit var binding: FragmentExpenseListTrackerBinding
    private lateinit var viewModel: ListExpenseViewModel
    private val expenseAdapter = ExpenseListAdapter(arrayListOf())
    private lateinit var budgetViewModel: ListBudgetViewModel
    private var budgetMap = mutableMapOf<Int, String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListExpenseViewModel::class.java)
        viewModel.refresh()

        binding.recViewExpense.adapter = expenseAdapter
        binding.recViewExpense.layoutManager = LinearLayoutManager(context)

        observeViewModel()


        binding.btnFab.setOnClickListener {
            val action = ExpenseListTrackerFragmentDirections.actionCreateExpenseFragment()
            Navigation.findNavController(it).navigate(action)
        }

        budgetViewModel = ViewModelProvider(this).get(ListBudgetViewModel::class.java)
        budgetViewModel.refresh()

        budgetViewModel.budgetLD.observe(viewLifecycleOwner, Observer { budgets ->
            budgetMap.clear()
            budgets.forEach { budgetMap[it.uuid] = it.name }
            // update adapter supaya nama budget muncul
            expenseAdapter.setBudgetMap(budgetMap)
        })
    }
    fun observeViewModel() {
        viewModel.expenseLD.observe(viewLifecycleOwner, Observer {
            expenseAdapter.updateExpenseList(it)
            if (it.isEmpty()) {
                binding.recViewExpense.visibility = View.GONE
                binding.txtError.text = "Your expense list is still empty."
            } else {
                binding.recViewExpense.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if (it == false) {
                binding.progressLoad3.visibility = View.GONE
            } else {
                binding.progressLoad3.visibility = View.VISIBLE
            }
        })

        viewModel.expenseLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if (it == false) {
                binding.txtError.visibility = View.GONE
            } else {
                binding.txtError.visibility = View.VISIBLE
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseListTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpenseListTrackerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}