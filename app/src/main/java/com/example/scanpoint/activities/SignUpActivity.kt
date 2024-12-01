package com.example.scanpoint.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.scanpoint.databinding.ActivitySignUpBinding
import com.example.scanpoint.states.States
import com.example.scanpoint.viewmodels.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: ViewModel

    private var auth = Firebase.auth

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModel()
        viewModel.getState().observe(this@SignUpActivity) {
            renderUi(it)
        }

        databaseReference = Firebase.database.reference

        println(databaseReference)

        binding.tvLogin.apply {
            text = addClickableLink("Login to your account?", "Login to your account?") {
                LoginActivity.launch(this@SignUpActivity)
            }

            movementMethod = LinkMovementMethod.getInstance()
        }

        binding.btnSignup.setOnClickListener{
            var isComplete: Boolean
            var isVerified: Boolean

            if (binding.etStudentName.text.isNullOrEmpty()) {
                binding.tilStudentName.error = "Required field!"
                isComplete = false
            }  else {
                binding.tilStudentName.error = null
                isComplete = true
            }

            if (binding.etStudentEmail.text.isNullOrEmpty()) {
                binding.tilStudentEmail.error = "Required field!"
                isComplete = false
            } else {
                binding.tilStudentEmail.error = null
                isComplete = true
            }

            if (binding.etStudentNumber.text.isNullOrEmpty()) {
                binding.tilStudentNumber.error = "Required field!"
                isComplete = false
            } else {
                binding.etStudentNumber.error = null
                isComplete = true
            }

            if (binding.etPassword.text.isNullOrEmpty()) {
                binding.tilPassword.error = "Required field!"
                isComplete = false
            } else {
                binding.etPassword.error = null
                isComplete = true
            }

            if (binding.etReEnterPass.text.isNullOrEmpty()) {
                binding.tilReEnterPass.error = "Required field!"
                isComplete = false
            } else {
                binding.etPassword.error = null
                isComplete = true
            }

            if (binding.etPassword.text.toString() != binding.etReEnterPass.text.toString()) {
                binding.tilReEnterPass.error = "Passwords do not match!"
                isVerified = false
            } else {
                isVerified = true
            }

            if (isComplete && isVerified) {
                val email = binding.etStudentEmail.text.toString()
                val password = binding.etPassword.text.toString()

                viewModel.signUp(email, password)
            }
        }
    }

    private fun renderUi (it: States) {
        when(it) {
            is States.SignedUp -> {
                val uid = auth.currentUser?.uid.toString()

                viewModel.createUserRecord(
                    uid,
                    binding.etStudentName.text.toString(),
                    binding.etStudentEmail.text.toString(),
                    binding.etStudentNumber.text.toString()
                )

                viewModel.signOut()
            }

            is States.SignedOut -> {
                LoginActivity.launch(this@SignUpActivity)
                finish()
            }

            else -> {}
        }
    }

    private fun addClickableLink(fullText: String, linkText: String, callback: () -> Unit): SpannableString {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                callback.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.BLACK

            }
        }

        val startIndex = fullText.indexOf(linkText, 0, true)

        return SpannableString(fullText).apply{
            setSpan(clickableSpan, startIndex, startIndex + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, SignUpActivity::class.java))
        }
    }
}