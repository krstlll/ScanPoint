package com.example.scanpoint.bottom_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanpoint.R
import com.example.scanpoint.adapters.EventAdapter
import com.example.scanpoint.databinding.FragmentScanBinding
import com.example.scanpoint.states.States
import com.example.scanpoint.viewmodels.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ViewfinderView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var auth = Firebase.auth

    private lateinit var binding: FragmentScanBinding
    private lateinit var captureManager: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var viewfinderView: ViewfinderView

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            renderUi(state)
        }

        barcodeScannerView = binding.zxingBarcodeScanner
        barcodeScannerView.setStatusText("")

        viewfinderView = binding.zxingViewfinderView

        captureManager = CaptureManager(requireActivity(), barcodeScannerView)
        captureManager.initializeFromIntent(requireActivity().intent, savedInstanceState)
        captureManager.setShowMissingCameraPermissionDialog(false)

        barcodeScannerView.decodeSingle { result ->
            viewModel.registerUser(auth.currentUser!!.uid, result.text)
        }
    }

    private fun renderUi(state: States) {
        when (state) {
            is States.RegisterSuccess -> {
                val newFragment = HomeFragment() // Replace with the fragment you want to navigate to
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, newFragment) // Replace R.id.fragment_container with your container ID
                    .addToBackStack(null) // Optional, adds to the back stack for back navigation
                    .commit()
            }

            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}