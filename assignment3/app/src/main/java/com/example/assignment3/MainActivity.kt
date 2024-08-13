package com.example.assignment3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views from the layout
        val inputData = findViewById<EditText>(R.id.inputData)
        val sendButton = findViewById<Button>(R.id.sendButton)

        // Set click listener for the Send Data button
        sendButton.setOnClickListener {
            val data = inputData.text.toString()
            Log.d("MainActivity", "Send Button Clicked, Data: $data")
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra("data_key", data) // Add the data to the intent
            }
            startActivity(intent) // Start SecondActivity
        }

        // Check and request POST_NOTIFICATIONS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Notification permission granted
                Log.d("MainActivity", "POST_NOTIFICATIONS permission granted")
            } else {
                // Notification permission denied
                Log.d("MainActivity", "POST_NOTIFICATIONS permission denied")
            }
        }
    }
}
