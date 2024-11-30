package com.example.scanpoint.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.scanpoint.R
import com.example.scanpoint.bottom_fragment.AddEventFragment
import com.example.scanpoint.bottom_fragment.HomeFragment
import com.example.scanpoint.bottom_fragment.NotificationFragment
import com.example.scanpoint.bottom_fragment.ProfileFragment
import com.example.scanpoint.bottom_fragment.RewardsFragment
import com.example.scanpoint.bottom_fragment.ScanFragment
import com.example.scanpoint.databinding.ActivityMainBinding
import com.example.scanpoint.databinding.ToolbarTitleBinding
import com.example.scanpoint.states.AuthenticationStates
import com.example.scanpoint.viewmodels.ViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : ViewModel

    private lateinit var fab:FloatingActionButton
    private lateinit var navigationView: BottomNavigationView
    private lateinit var toolbarTitleBinding: ToolbarTitleBinding

    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbarTitleBinding = ToolbarTitleBinding.bind(binding.root)
        setContentView(binding.root)

        fab = findViewById(R.id.fab)
        navigationView = findViewById(R.id.nav)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getState().observe(this@MainActivity) {
            renderUi(it)
        }

        viewModel.getUserInfo()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            navigationView.selectedItemId = R.id.nav_home
        }

        fab.setOnClickListener {
            if (auth.currentUser?.uid.toString() == "9nJEthWX6bOoSN4qlafsVqR7z0c2") {
                replaceFragment(AddEventFragment())
            } else {
                replaceFragment(ScanFragment())
            }
        }

        navigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replace(HomeFragment())
                    toolbarTitleBinding.toolbarTitle.text ="Hey!"
                    toolbarTitleBinding.toolbarSubtitle.text ="Welcome Back"

                }
                R.id.nav_user -> {
                    replace(ProfileFragment())
                    toolbarTitleBinding.toolbarTitle.text ="Profile"
                    toolbarTitleBinding.toolbarSubtitle.text ="Information"
                }
                R.id.nav_rewards -> {
                    replace(RewardsFragment())
                    toolbarTitleBinding.toolbarTitle.text ="Rewards"
                    toolbarTitleBinding.toolbarSubtitle.text ="Your Achievements"
                }
                R.id.nav_notification -> {
                    replace(NotificationFragment())
                    toolbarTitleBinding.toolbarTitle.text ="Notifications"
                    toolbarTitleBinding.toolbarSubtitle.text ="Stay updated!"
                }
            }
            true
        }

        toolbarTitleBinding.includeToolbar.setOnApplyWindowInsetsListener { view, insets ->
            // Apply padding to the top for the status bar
            view.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }

        binding.bottomAppBar.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = 0)
            insets
        }
    }

    private fun renderUi (it: AuthenticationStates) {
        when(it) {
            is AuthenticationStates.Default -> {
                if (auth.currentUser?.uid.toString() == "9nJEthWX6bOoSN4qlafsVqR7z0c2") {
                    fab.setImageResource(R.drawable.add)
                }
            }

            else -> {

            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.view, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun replace(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.view, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}
