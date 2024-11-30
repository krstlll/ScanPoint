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
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.scanpoint.R
import com.example.scanpoint.databinding.ActivityLoginBinding
import com.example.scanpoint.states.AuthenticationStates
import com.example.scanpoint.viewmodels.ViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel : ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModel()
        viewModel.getState().observe(this@LoginActivity) {
            renderUi(it)
        }


        val loginButton = findViewById<Button>(R.id.btn_login)

        loginButton.setOnClickListener {
            val email = binding.etStudentNumber.text.toString()
            val password = binding.etPassword.text.toString()

            viewModel.signIn(email, password)
        }

        binding.tvSignUp.apply {
            text = addClickableLink("Sign Up", "Sign Up") {
                SignUpActivity.launch(this@LoginActivity)
            }

            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.isUserSignedIn()
    }

    private fun renderUi (it: AuthenticationStates) {
        when(it) {
            is AuthenticationStates.AlreadySignedIn -> {
                if (it.alreadySignedIn) {
                    MainActivity.launch(this@LoginActivity)
                    finish()
                }
            }

            is AuthenticationStates.SignedIn -> {
                MainActivity.launch(this@LoginActivity)
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
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}