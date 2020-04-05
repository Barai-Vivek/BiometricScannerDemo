package com.app.biometricscanner.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.app.biometricscanner.HelperClass
import com.app.biometricscanner.activities.LockScreenActivity
import com.app.biometricscanner.activities.SplashScreenActivity

class ApplicationClass : Application() {
    private var context: Context? = null
    private var helperClass: HelperClass? = null

    init {
        instance = this
    }

    companion object {
        private var instance: ApplicationClass? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = ApplicationClass.applicationContext()
        helperClass = HelperClass(this@ApplicationClass)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                //prevent opening lock screen opening again and again
                if (helperClass!!.loadPreferences(helperClass!!.LOCK_PAUSE_TIME).isEmpty()) {
                    helperClass!!.savePreferences(helperClass!!.LOCK_PAUSE_TIME, "0")
                }
                val pauseTime = helperClass!!.loadPreferences(helperClass!!.LOCK_PAUSE_TIME).toLong()
                val currentTime = System.currentTimeMillis()
                var LOCK_INTERVAL_TIME: Long = 0
                if (pauseTime > 0 && !helperClass!!.loadPreferences(helperClass!!.MINUTES_TO_LOCK).isEmpty()) {
                    LOCK_INTERVAL_TIME = (helperClass!!.loadPreferences(helperClass!!.MINUTES_TO_LOCK).toInt() * 60 * 1000).toLong()
                }
                if (activity !is LockScreenActivity
                        && helperClass!!.loadBoolPreferences(helperClass!!.IS_APP_LOCK_ACTIVE)
                        && activity !is SplashScreenActivity
                        && pauseTime > 0 && !helperClass!!.loadPreferences(helperClass!!.MINUTES_TO_LOCK).isEmpty()
                        && currentTime - pauseTime >= LOCK_INTERVAL_TIME) {
                    val intent = Intent(context, LockScreenActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (activity !is SplashScreenActivity
                        && activity !is LockScreenActivity
                        && helperClass!!.loadBoolPreferences(helperClass!!.IS_APP_LOCK_ACTIVE)) {
                    helperClass!!.savePreferences(helperClass!!.LOCK_PAUSE_TIME, System.currentTimeMillis().toString() + "")
                } else if (activity is LockScreenActivity) {
                    helperClass!!.savePreferences(helperClass!!.LOCK_PAUSE_TIME, "0")
                }
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}