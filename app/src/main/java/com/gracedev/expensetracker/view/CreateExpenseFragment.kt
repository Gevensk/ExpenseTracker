package com.gracedev.expensetracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gracedev.expensetracker.R
import com.gracedev.expensetracker.databinding.FragmentCreateExpenseBinding
import com.gracedev.expensetracker.viewmodel.ListBudgetViewModel
import com.gracedev.expensetracker.viewmodel.ListExpenseViewModel
import java.text.NumberFormat
import java.util.Locale
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class CreateExpenseFragment : Fragment() {
    private lateinit var binding: FragmentCreateExpenseBinding
    private lateinit var viewModel: ListBudgetViewModel
    private lateinit var expenseViewModel: ListExpenseViewModel
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseViewModel = ViewModelProvider(this).get(ListExpenseViewModel::class.java)

        sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("uuid", -1)  // -1 sebagai default jika tidak ditemukan

        viewModel = ViewModelProvider(this).get(ListBudgetViewModel::class.java)
        viewModel.refresh(userId)

        viewModel.budgetLD.observe(viewLifecycleOwner, Observer { budgets ->
            val spinnerItems = budgets.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter

            // Saat item dipilih
            binding.spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedBudget = budgets[position]
                    val budgetId = selectedBudget.uuid
                    val totalBudget = selectedBudget.budget


                    // Observe total expense dari ViewModel
                    expenseViewModel.getTotalExpenseByBudgetId(budgetId, userId)
                        .observe(viewLifecycleOwner) { totalExpense ->
                            val used = totalExpense ?: 0
                            val remaining = totalBudget - used

                            // Set progress bar
                            binding.progressBar.max = totalBudget
                            binding.progressBar.progress = used

                            // Update TextView
                            binding.txtUsedBudget.text = "IDR ${formatter.format(used)}"
                            binding.txtTotalBudget.text = "IDR ${formatter.format(totalBudget)}"

                            binding.txtNominal.hint = "Nominal (IDR ${formatter.format(remaining)} left)"
                        }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            })

            binding.btnAdd.setOnClickListener {
                val selectedPosition = binding.spinner.selectedItemPosition
                viewModel.budgetLD.value?.let { budgets ->
                    val selectedBudget = budgets[selectedPosition]
                    val budgetId = selectedBudget.uuid
                    val totalBudget = selectedBudget.budget

                    val name = binding.txtNotes.text.toString()
                    val nominal = binding.txtNominal.text.toString().toIntOrNull() ?: 0

                    val formatDate = SimpleDateFormat("dd MMMM yyyy hh.mm a", Locale("in", "ID"))
                    val dateParsed = formatDate.parse(binding.txtDate.text.toString())
                    val date = (dateParsed?.time ?: System.currentTimeMillis()) / 1000L

                    expenseViewModel.getTotalExpenseByBudgetId(budgetId, userId)
                        .observe(viewLifecycleOwner) { totalExpense ->
                            val used = totalExpense ?: 0
                            val remaining = totalBudget - used

                            if (nominal <= 0) {
                                Toast.makeText(
                                    requireContext(),
                                    "Pengeluaran tidak boleh kurang atau sama dengan 0",
                                    Toast.LENGTH_LONG
                                ).show()
                            }else if (nominal > remaining) {
                                Toast.makeText(
                                    requireContext(),
                                    "Pengeluaran melebihi sisa budget (tersisa Rp${formatter.format(remaining)})",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val newExpense = com.gracedev.expensetracker.model.Expense(
                                    name = name,
                                    nominal = nominal,
                                    date = date.toInt(),
                                    budgetId = budgetId,
                                    userId = userId
                                )

                                val detailViewModel = ViewModelProvider(this)[com.gracedev.expensetracker.viewmodel.DetailExpenseViewModel::class.java]
                                detailViewModel.addExpense(listOf(newExpense))

                                Toast.makeText(requireContext(), "Pengeluaran ditambahkan", Toast.LENGTH_SHORT).show()

                                binding.txtNominal.setText("")
                                binding.txtNotes.setText("")

                                Navigation.findNavController(it).popBackStack()
                            }
                        }
                }
            }

            val defaultDate = SimpleDateFormat("dd MMMM yyyy hh.mm a", Locale("in", "ID")).format(Date())
            binding.txtDate.text = defaultDate

            binding.txtDate.setOnClickListener {
                showDatePicker()
            }

        })
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

            val currentTime = Calendar.getInstance()
            selectedCalendar.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY))
            selectedCalendar.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE))
            selectedCalendar.set(Calendar.SECOND, 0)

            val formatter = SimpleDateFormat("dd MMMM yyyy hh.mm a", Locale("in", "ID"))
            val formattedDateTime = formatter.format(selectedCalendar.time)

            binding.txtDate.text = formattedDateTime

        }, year, month, day)

        datePicker.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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