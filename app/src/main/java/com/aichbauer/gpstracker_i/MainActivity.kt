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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.q11.R


class MainActivity : AppCompatActivity(), LocationListener {

    //Initializing Helper-Variables for locationMagaser Usage and Output
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
            //Add Longitude and Latitude to intent-Variables for Handling in next view: AddTaskActivity
            intent.putExtra("EXTRA_LONGITUDE", myLongitude.toString());
            intent.putExtra("EXTRA_LATTITUDE", myLattitude.toString());
            // start your next activity
            startActivity(intent)
        }

        val buttonGetThirdLocation: Button = findViewById(R.id.btnStartGetThirdLocation)
        buttonGetThirdLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            // start your next activity
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
    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        tvGpsLocation = findViewById(R.id.textView)
        tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude

        //Set myLongitude, myLattitude to pass these via intent.putExtra to AddTaskActivity
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


    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

}