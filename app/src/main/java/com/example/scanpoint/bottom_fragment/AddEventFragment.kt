package com.example.scanpoint.bottom_fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.scanpoint.R
import com.example.scanpoint.activities.LoginActivity
import com.example.scanpoint.databinding.FragmentAddEventBinding
import com.example.scanpoint.databinding.FragmentProfileBinding
import com.example.scanpoint.states.AuthenticationStates
import com.example.scanpoint.viewmodels.ViewModel
import java.util.Calendar

class AddEventFragment : Fragment() {

    private lateinit var calendar: Calendar
    private lateinit var datePickerDialog: DatePickerDialog

    private lateinit var viewModel: ViewModel
    private lateinit var binding: FragmentAddEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            renderUi(state)
        }

        calendar = Calendar.getInstance()

        binding.etEventDate.setOnClickListener {
            showDatePicker(binding.etEventDate)
        }

        binding.btnAddEvent.setOnClickListener {
            var isComplete = true

            val eventDate = binding.etEventDate.text.toString()

            if (eventDate.isBlank()) {
                Toast.makeText(requireContext(), "Please enter a valid event date", Toast.LENGTH_SHORT).show()
                isComplete = false
            }

            if (binding.etEventName.text.isNullOrEmpty()) {
                binding.tilEventName.error = "Required field!"
                isComplete = false
            } else {
                binding.tilEventName.error = null
            }

            if (binding.etLocation.text.isNullOrEmpty()) {
                binding.tilLocation.error = "Required field!"
                isComplete = false
            } else {
                binding.tilLocation.error = null
            }

            println("Event Date: '$eventDate'")
            println("Event Date: '$eventDate'")

            if (isComplete) {
                val eventName = binding.etEventName.text.toString()
                val eventVenue = binding.etLocation.text.toString()

                viewModel.createEvent(eventName, eventDate, eventVenue)
            }
        }

        return binding.root
    }

    private fun renderUi(state: AuthenticationStates) {
        when (state) {
            is AuthenticationStates.EventCreateSuccess -> {
                binding.etEventName.setText("")
                binding.etEventDate.setText("")
                binding.etLocation.setText("")
            }

            else -> {}
        }
    }

    private fun showDatePicker(editText: EditText) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(selectedDate)
            }, year, month, day)
        datePickerDialog.show()
    }
}
