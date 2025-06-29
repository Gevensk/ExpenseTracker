package com.gracedev.expensetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.gracedev.expensetracker.databinding.ActivityMainBinding
import com.gracedev.expensetracker.databinding.ActivitySignBinding

class SignActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val isLogin = sharedPref.getBoolean("isLogin", false)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentHost) as NavHostFragment
        navController = navHostFragment.navController

        if (isLogin == true) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}