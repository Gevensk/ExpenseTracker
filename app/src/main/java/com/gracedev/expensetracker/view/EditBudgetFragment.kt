package com.gracedev.expensetracker.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentCreateBudgetingBinding
import com.gracedev.expensetracker.viewmodel.DetailBudgetViewModel


class EditBudgetFragment : Fragment() {
    private lateinit var binding:FragmentCreateBudgetingBinding
    private lateinit var viewModel: DetailBudgetViewModel

    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("uuid", -1)  // -1 sebagai default jika tidak ditemukan

        viewModel = ViewModelProvider(this).get(DetailBudgetViewModel::class.java)

        // retrieve the argument from clicked imgEdit
        val uuid = EditBudgetFragmentArgs.fromBundle(requireArguments()).uuid
        // pass the uuid argument to DetailViewModel (fetch function)
        viewModel.fetch(uuid, userId)
        // start to observe from LiveData and
        // put the data into the TextViews
        observeViewModel()

        binding.txtJudulBudget.text = "Edit Budget"
        binding.btnAdd.text = "Save Changes"
        viewModel.fetchTotalExpense(uuid, userId)
        viewModel.totalExpenseLD.observe(viewLifecycleOwner, Observer { total ->
            val totalExpense = total ?: 0

            binding.btnAdd.setOnClickListener {
                val newBudget = binding.txtNominal.text.toString().toInt()
                val name = binding.txtNama.text.toString()

                if (newBudget <= 0) {
                    Toast.makeText(
                        requireContext(),
                        "Budget tidak boleh kurang atau sama dengan 0",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (newBudget < totalExpense) {
                    Toast.makeText(
                        requireContext(),
                        "Budget tidak boleh lebih kecil dari total pengeluaran: $totalExpense",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.update(name, newBudget, uuid, userId)
                    Toast.makeText(view.context, "Budget updated", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(it).popBackStack()
                }
            }
        })
    }

    fun observeViewModel() {
        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            binding.txtNama.setText(it.name)
            binding.txtNominal.setText(it.budget.toString())

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
        binding = FragmentCreateBudgetingBinding.inflate(inflater,container, false)
        return binding.root

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditBudgetFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}