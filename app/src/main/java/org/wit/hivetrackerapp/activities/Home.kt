package org.wit.hivetrackerapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.adapters.HiveTrackerAdapter
import org.wit.hivetrackerapp.databinding.HomeBinding
import org.wit.hivetrackerapp.fragments.UpdateFragment
import org.wit.hivetrackerapp.models.HiveModel
import timber.log.Timber

class Home : AppCompatActivity(), HiveTrackerAdapter.Communicator {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val mFragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.addFragment,
            R.id.listFragment,
            R.id.aboutusFragment,
            //R.id.updateFragment,
            R.id.accountDetailsFragment,
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @SuppressLint("WrongConstant")
    override fun passDataCom(hive: HiveModel) {
        val mFragment = UpdateFragment()
        Timber.i("testing data sent to home page : $hive")
        val mBundle = Bundle()
        mBundle.putParcelable("data",hive)
        mFragment.arguments=mBundle
        mFragment.data = hive
        Timber.i("testing data sent to home page : $mBundle")
        mFragmentManager.beginTransaction().replace(R.id.update,mFragment).addToBackStack(null).commit()

    }



}