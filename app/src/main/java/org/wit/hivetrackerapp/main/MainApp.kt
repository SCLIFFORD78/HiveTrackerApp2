package org.wit.hivetrackerapp.main

import android.app.Application
import org.wit.hivetrackerapp.models.*
import org.wit.usertrackerapp.models.UserJSONStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var hives: HiveStore
    lateinit var users: UserStore
    lateinit var loggedInUser: UserModel


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        hives = HiveJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        loggedInUser = UserModel()
        //hives = HiveMemStore()
        i("HiveTracker started")
        //hives.add(HiveModel("One", "About one..."))
        //hives.add(HiveModel("Two", "About two..."))
        //hives.add(HiveModel("Three", "About three..."))
    }
}
