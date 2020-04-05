package com.app.biometricscanner.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.biometricscanner.HelperClass
import com.app.biometricscanner.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var helperClass: HelperClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helperClass = HelperClass(this@MainActivity)

        if (helperClass.loadPreferences(helperClass.MINUTES_TO_LOCK) == "") {
            helperClass.savePreferences(helperClass.MINUTES_TO_LOCK, "2")
        }

        changeTextAndControls()

        switchActivateLock.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                helperClass.saveBoolPreferences(helperClass.IS_APP_LOCK_ACTIVE, isChecked)
                changeTextAndControls()
                hideKeyboard(this@MainActivity)
            }
        }

        btnSave.setOnClickListener {
            hideKeyboard(this@MainActivity)
            val time = edtMinutesToLock.text.toString()
            if (time.toInt() > 0) {
                helperClass.savePreferences(helperClass.MINUTES_TO_LOCK, time)
                txtAppLockText.text = resources.getString(R.string.app_lock_msg, helperClass.loadPreferences(helperClass.MINUTES_TO_LOCK))
                Toast.makeText(this@MainActivity, "Setting saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Please enter minimum 1 minute time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun changeTextAndControls(){
        if(helperClass.loadBoolPreferences(helperClass.IS_APP_LOCK_ACTIVE)) {
            switchActivateLock.isChecked = true
            relMain.visibility = View.VISIBLE
            edtMinutesToLock.setSelection(edtMinutesToLock.text.length)
            edtMinutesToLock.setText(helperClass.loadPreferences(helperClass.MINUTES_TO_LOCK))
            txtAppLockText.text = resources.getString(R.string.app_lock_msg, helperClass.loadPreferences(helperClass.MINUTES_TO_LOCK))
        } else {
            switchActivateLock.isChecked = false
            relMain.visibility = View.GONE
            edtMinutesToLock.setText("")
            txtAppLockText.text = resources.getString(R.string.system_lock_is_deactive)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}