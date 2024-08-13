package com.example.assignment3

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.util.Log

class SecondActivity : AppCompatActivity() {

    private val CHANNEL_ID = "data_notification_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Receive data from the intent
        val receivedData = intent.getStringExtra("data_key")
        Log.d("SecondActivity", "Received Data: $receivedData")

        // Display the received data
        val dataDisplay = findViewById<TextView>(R.id.dataDisplay)
        dataDisplay.text = receivedData

        // Create notification channel and check for permissions before showing the notification
        createNotificationChannel()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, show the notification
            showNotification(receivedData)
        } else {
            // Permission is not granted, request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
        }
    }

    // Create a notification channel (required for Android 8.0+)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Data Notification"
            val descriptionText = "Channel for displaying received data"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Show a notification with the received data
    private fun showNotification(data: String?) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Set your notification icon here
            .setContentTitle("Received Data")
            .setContentText(data)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            try {
                notify(0, builder.build())
            } catch (e: SecurityException) {
                Log.e("SecondActivity", "Notification permission not granted: ${e.message}")
            }
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, show the notification
                val receivedData = intent.getStringExtra("data_key")
                showNotification(receivedData)
            } else {
                // Permission denied, handle the case
                Log.d("SecondActivity", "POST_NOTIFICATIONS permission denied")
            }
        }
    }
}
