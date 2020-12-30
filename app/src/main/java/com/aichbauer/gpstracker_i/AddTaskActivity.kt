package com.aichbauer.gpstracker_i

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.q11.R

class AddTaskActivity : AppCompatActivity() {

    //Declare Hanlder-Variable for Database-Connection
   private val db = Database(this)


    //Override Functions
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task_activity)

        //Declare Handler-Variables for Programm Logic (bound to view-elements)
        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editDescription = findViewById<EditText>(R.id.editDescription)

        //Declare Handler-Variables for Output-Elements
        val outputTitle: TextView = findViewById<TextView>(R.id.outputTitle);
        val outputText: TextView = findViewById<TextView>(R.id.outputText);

        //Set HandlerVariable for Button:btnOK
        val buttonOk: Button = findViewById(R.id.btnOk)

        //setOnClickListener for Button:btnOK
        buttonOk.setOnClickListener {
            //Show editTitle on Screen (TextView outputText)
            outputTitle.setText(editTitle.text.toString());
            outputText.setText(editDescription.text.toString());

            val latitude = 0.0;
            val longitude = 0.0;

            //Save entry to database
            println("try to save entry to database");
           db.insertActivity(myActivity(0, System.currentTimeMillis(), editTitle.text.toString(), editDescription.text.toString(), latitude, longitude))

            //Set intent to Show ListActivity
           val intent = Intent(this, ListActivity::class.java)
           startActivity(intent)
        }
    }


}