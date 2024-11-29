package com.example.scanpoint.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.scanpoint.R
import com.example.scanpoint.bottom_fragment.AddEventFragment
import com.example.scanpoint.bottom_fragment.HomeFragment
import com.example.scanpoint.bottom_fragment.NotificationFragment
import com.example.scanpoint.bottom_fragment.ProfileFragment
import com.example.scanpoint.bottom_fragment.RewardsFragment
import com.example.scanpoint.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navigationView: BottomNavigationView
    private lateinit var fab:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fab = findViewById(R.id.fab)
        navigationView = findViewById(R.id.nav)


        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            navigationView.selectedItemId = R.id.nav_home
        }

        fab.setOnClickListener {
            replaceFragment(AddEventFragment())
        }

        navigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replace(HomeFragment())
                R.id.nav_user -> replace(ProfileFragment())
                R.id.nav_rewards -> replace(RewardsFragment())
                R.id.nav_notification -> replace(NotificationFragment())
            }
            true
        }

        binding.bottomAppBar.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = 0)
            insets
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
}
