package com.example.scanpoint.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scanpoint.R
import com.example.scanpoint.databinding.ActivityEventDetailsBinding
import com.example.scanpoint.databinding.ActivityLoginBinding
import com.example.scanpoint.states.States
import com.example.scanpoint.viewmodels.ViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailsBinding
    private lateinit var viewModel : ViewModel

    private var eventId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventId = intent.getStringExtra("event_uid")

        viewModel = ViewModel()
        viewModel.getState().observe(this@EventDetailsActivity) {
            renderUi(it)
        }

        viewModel.fetchEvenDetails(eventId.toString())

        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(eventId, BarcodeFormat.QR_CODE, 280, 280)

        binding.ivQrHolder.setImageBitmap(bitmap)

        binding.btnBack.setOnClickListener {
            MainActivity.launch(this@EventDetailsActivity)
            finish()
        }
    }

    private fun renderUi (it: States) {
        when(it) {
            is States.EventDetailsFetched -> {
                binding.tvEventName.text = it.event?.eventName
                binding.tvEventDate.text = it.event?.eventDate
                binding.tvEventVenue.text = it.event?.eventVenue
            }

            else -> {}
        }
    }

    companion object {
        fun launch(activity : Activity) {
            activity.startActivity(Intent(activity, EventDetailsActivity::class.java))
        }
    }
}