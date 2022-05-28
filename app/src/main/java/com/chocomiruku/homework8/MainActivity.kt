package com.chocomiruku.homework8

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chocomiruku.homework8.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        createNotificationChannel()

        binding.getFcmTokenBtn.setOnClickListener {
            if (checkGooglePlayServices()) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.i(TAG, "Error: Failed to get FCM token", task.exception)
                        return@OnCompleteListener
                    }

                    val token = task.result
                    Log.d(TAG, token)
                })
            } else showSnackBarNoGoogleServicesAvailable()
        }

        setContentView(binding.root)
    }

    private fun createNotificationChannel() {
        val name = "test_channel"
        val descriptionText = "test_channel_description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return if (status != ConnectionResult.SUCCESS) {
            Log.i(TAG, "Error: No GooglePlay services available")
            false
        } else {
            true
        }
    }

    private fun showSnackBarNoGoogleServicesAvailable() {
        Snackbar.make(binding.getFcmTokenBtn, R.string.no_google_services, Snackbar.LENGTH_LONG)
            .show()
    }

    companion object {
        private const val CHANNEL_ID = "default_channel_id"
        private const val TAG = "MainActivityLog"
    }
}