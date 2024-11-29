package com.example.scanpoint.bottom_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.scanpoint.R
import java.util.Calendar

class AddEventFragment : Fragment() {
    private lateinit var editText: EditText
    private lateinit var button: Button

    private lateinit var calendar: Calendar
    private lateinit var  datePickerDialog: DatePickerDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_event, container, false)

        calendar = Calendar.getInstance()
        editText = view.findViewById(R.id.et_eventDate)
        button = view.findViewById(R.id.btn_add_event)

        editText.setOnClickListener {
            showDatePicker(editText)
        }

        button.setOnClickListener {
            val event_date = editText.text.toString()
            if (event_date.isNotBlank()) {
                Toast.makeText(requireContext(), "The Event will be on $event_date", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter a valid event date", Toast.LENGTH_SHORT).show()
            }
        }

        editText.setOnClickListener {
            showDatePicker(editText)
        }

        return view
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
