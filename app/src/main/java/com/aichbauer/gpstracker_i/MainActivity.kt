package com.aichbauer.gpstracker_i

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.q11.R

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2
    private var myLongitude = 0.0
    private var myLattitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "GPS Location Tracking System"
        val button: Button = findViewById(R.id.getLocation)
        button.setOnClickListener {
            getLocation()
        }

        val buttonGetSecondLocation: Button = findViewById(R.id.btnStartGetSecondLocation)
        buttonGetSecondLocation.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            // start your next activity
            intent.putExtra("EXTRA_LONGITUDE", myLongitude);
            intent.putExtra("EXTRA_LATTITUDE", myLattitude);
            startActivity(intent)
        }
    }
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(@NonNull location: Location) {
        tvGpsLocation = findViewById(R.id.textView)
        tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
        myLongitude = location.longitude.toDouble()
        myLattitude = location.latitude.toDouble()

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}