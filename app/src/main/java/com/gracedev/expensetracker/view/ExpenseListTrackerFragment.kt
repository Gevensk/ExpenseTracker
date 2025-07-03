package com.gracedev.expensetracker.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentExpenseListTrackerBinding
import com.gracedev.expensetracker.model.Expense
import com.gracedev.expensetracker.viewmodel.ListBudgetViewModel
import com.gracedev.expensetracker.viewmodel.ListExpenseViewModel


class ExpenseListTrackerFragment : Fragment() {
    private lateinit var binding: FragmentExpenseListTrackerBinding
    private lateinit var viewModel: ListExpenseViewModel
    private lateinit var expenseAdapter: ExpenseListAdapter
    private lateinit var budgetViewModel: ListBudgetViewModel
    private var budgetMap = mutableMapOf<Int, String>()
    private lateinit var expense: ArrayList<Expense>

    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("uuid", -1)  // -1 sebagai default jika tidak ditemukan

        expenseAdapter = ExpenseListAdapter(arrayListOf()) { selectedExpense ->
            val dialog = ExpenseDetailFragment.newInstance(selectedExpense)
            dialog.show(parentFragmentManager, "ExpenseDetail")
        }
        binding.recViewExpense.adapter = expenseAdapter

        viewModel = ViewModelProvider(this).get(ListExpenseViewModel::class.java)
        viewModel.refresh(userId)

        binding.recViewExpense.adapter = expenseAdapter
        binding.recViewExpense.layoutManager = LinearLayoutManager(context)

        observeViewModel()


        binding.btnFab.setOnClickListener { view ->
            if (budgetMap.isEmpty()) {
                Toast.makeText(requireContext(), "Please create a budget before adding expenses.", Toast.LENGTH_SHORT).show()
            } else {
                val action = ExpenseListTrackerFragmentDirections.actionCreateExpenseFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }

        budgetViewModel = ViewModelProvider(this).get(ListBudgetViewModel::class.java)
        budgetViewModel.refresh(userId)

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