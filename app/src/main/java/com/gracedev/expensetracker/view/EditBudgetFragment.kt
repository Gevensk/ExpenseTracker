package com.gracedev.expensetracker.view

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailBudgetViewModel::class.java)

        // retrieve the argument from clicked imgEdit
        val uuid = EditBudgetFragmentArgs.fromBundle(requireArguments()).uuid
        // pass the uuid argument to DetailViewModel (fetch function)
        viewModel.fetch(uuid)
        // start to observe from LiveData and
        // put the data into the TextViews
        observeViewModel()

        binding.txtJudulBudget.text = "Edit Budget"
        binding.btnAdd.text = "Save Changes"
        binding.btnAdd.setOnClickListener {
            viewModel.update(
                binding.txtNama.text.toString(),
                binding.txtNominal.text.toString().toInt(),
                uuid)
            Toast.makeText(view.context, "Budget updated", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(it).popBackStack()
        }

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