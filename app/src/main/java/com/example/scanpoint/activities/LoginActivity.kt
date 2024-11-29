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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scanpoint.R
import com.example.scanpoint.databinding.ActivityLoginBinding
import com.example.scanpoint.databinding.ActivitySignUpBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = findViewById<Button>(R.id.btn_login)



        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvSignUp.apply {
            text = addClickableLink("Sign Up", "Sign Up") {
                SignUpActivity.launch(this@LoginActivity)
            }

            movementMethod = LinkMovementMethod.getInstance()
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