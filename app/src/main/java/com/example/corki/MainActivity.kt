package com.example.corki

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.corki.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPreferences()
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_search, R.id.navigation_create, R.id.navigation_my_offers,
                R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.visibility = View.GONE

        supportActionBar?.hide()
    }

    fun bottomNavVisible() {
        binding.navView.visibility = View.VISIBLE
    }

    fun bottomNavGone() {
        binding.navView.visibility = View.GONE
    }

    //JWT
    private val preferencesFileName = "com.example.corki"
    private val TOKEN_KEY = "TOKEN"
    private lateinit var preferences: SharedPreferences

    private fun initPreferences() {
        preferences = getSharedPreferences(preferencesFileName, MODE_PRIVATE)
    }

    fun getJWT(): String {
        return preferences.getString("TOKEN", "")!!
    }

    fun clearJWT() {
        preferences.edit().clear().apply()
    }

    fun putJWT(token: String) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

}