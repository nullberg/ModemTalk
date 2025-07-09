package com.nullberg.modemtalk

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nullberg.modemtalk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf( R.id.nav_home, R.id.nav_modemspecs, R.id.nav_telephony, R.id.nav_modemtalk  ), drawerLayout  )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // ---------  added to fix cutting off on pixel 6a ------
        val headerView = navView.getHeaderView(0)
        val rootLayout = headerView.findViewById<View>(R.id.nav_header_root)

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val originalTopPadding = view.resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)

            view.setPadding(
                view.paddingLeft,
                systemInsets.top + originalTopPadding,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
        // -------------


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}

//        AlertDialog.Builder(this)
//            .setTitle("Test Dialog")
//            .setMessage("Button clicked!")
//            .setPositiveButton("OK", null)
//            .show()