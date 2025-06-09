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
import com.gracedev.expensetracker.databinding.FragmentBudgetListBinding
import com.gracedev.expensetracker.viewmodel.ListBudgetViewModel


class BudgetListFragment : Fragment() {
    private lateinit var binding:FragmentBudgetListBinding
    private lateinit var viewModel:ListBudgetViewModel
    private val todoListAdapter  = BudgetListAdapter(arrayListOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListBudgetViewModel::class.java)
        viewModel.refresh()
        binding.recViewBudget.layoutManager = LinearLayoutManager(context)
        binding.recViewBudget.adapter = todoListAdapter

        binding.btnFab.setOnClickListener {
            val action = BudgetListFragmentDirections.actionCreateBudget()
            Navigation.findNavController(it).navigate(action)
        }
        observeViewModel()

    }

    fun observeViewModel() {
        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            todoListAdapter.updateTodoList(it)
            if(it.isEmpty()) {
                binding.recViewBudget?.visibility = View.GONE
                binding.txtError.setText("Your budget list still empty.")
            } else {
                binding.recViewBudget?.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.progressLoad?.visibility = View.GONE
            } else {
                binding.progressLoad?.visibility = View.VISIBLE
            }
        })

        viewModel.budgetLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.txtError?.visibility = View.GONE
            } else {
                binding.txtError?.visibility = View.VISIBLE
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
        binding = FragmentBudgetListBinding.inflate(inflater,container,false)
        return  binding.root

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BudgetListFragment().apply {

            }
    }
}