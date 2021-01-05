package com.aichbauer.gpstracker_i

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.q11.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_note_edit.*
import java.util.*


class NoteEditActivity : AppCompatActivity(), View.OnClickListener, DialogInterface.OnClickListener {

    private val db = Database(this)
    private var note: myActivity? = null
    private var lastLocation: Location? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        btnSave.setOnClickListener(this)

        val id = intent.getLongExtra("id", -1)
        if (id >= 0) {
            note = db.getNote(id)
            etTitle.setText(note?.title)
            etMessage.setText(note?.message)

            if (note?.latitude != 0.0) {
                tvLocation.visibility = View.VISIBLE
                tvLocation.text = "Location: " + note!!.latitude + ", " + note!!.longitude
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Init Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()

        updateLastLocation()
    }

    private fun updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->  lastLocation = task.result
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            101
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.menu_note_edit, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*if (item.itemId == R.id.delete) {
            AlertDialog.Builder(this)
                .setMessage(R.string.delete_message)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, null)
                .show()
        } else */ if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }

    private fun saveNote() {
        val title = etTitle.text.toString()
        val message = etMessage.text.toString()
        val db = Database(this)

        if (note == null) {
            lastLocation?.let {
                latitude = it.latitude
                longitude = it.longitude
            }
            db.insertActivity(myActivity(0, System.currentTimeMillis(), title, message, latitude, longitude))
        } else {
            note!!.message = message
            note!!.title = title
            db.updateNote(note!!)
        }

        val geocoder = Geocoder(this, Locale.getDefault())
        //val addresses: List<*> = geocoder.getFromLocation(latitude, longitude, 1)
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        println("here comes the adressses:")
        println(addresses)

        val address = getAddress(latitude, longitude)
        println("here comes the adress:")
        println(address)

        //vibrate()
       // MediaPlayer.create(this, R.raw.beep).start()
        //Toast.makeText(this, R.string.note_saved, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }


    override fun onClick(v: View?) {
        val intent = Intent(this, MainActivity::class.java)
        // start your next activity
        startActivity(intent)
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        note.run {
            db.deleteNote(note!!)
        }
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLastLocation()
            }
        }
    }
}