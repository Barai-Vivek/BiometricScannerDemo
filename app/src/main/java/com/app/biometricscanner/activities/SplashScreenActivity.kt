package com.app.biometricscanner.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.biometricscanner.HelperClass
import com.app.biometricscanner.R


class SplashScreenActivity : AppCompatActivity() {
    val LOCK_SCREEN_RESULT = 578
    private lateinit var helperClass: HelperClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        helperClass = HelperClass(this@SplashScreenActivity)
        if (Build.VERSION.SDK_INT >= 16) {
            val window = this.window
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            if (Build.VERSION.SDK_INT >= 21) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = Color.TRANSPARENT
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                if (Build.VERSION.SDK_INT >= 23) { //Set the status bar white and the notifications grey
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.statusBarColor = Color.TRANSPARENT
                    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                }
            }
        }

        Handler().postDelayed(Runnable {
            if(helperClass.loadBoolPreferences(helperClass.IS_APP_LOCK_ACTIVE)) {
                val intent = Intent(applicationContext, LockScreenActivity::class.java)
                intent.putExtra("camefrom", "splash_screen")
                startActivityForResult(intent, LOCK_SCREEN_RESULT)
            } else {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCK_SCREEN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val returnValue: String = data!!.getStringExtra("lockscreen")
                if (returnValue.equals("finished")) {
                    Toast.makeText(this@SplashScreenActivity, "You have unlocked app successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}