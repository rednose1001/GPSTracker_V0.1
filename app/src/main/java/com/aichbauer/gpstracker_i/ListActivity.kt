package com.aichbauer.gpstracker_i

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import com.example.q11.R
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity(),View.OnClickListener, AdapterView.OnItemClickListener  {

    //Initialize HelperVariables for local Database and noteAdapter Usage
    private var db = Database(this)
    private var noteAdapter: myActivityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val lvNotes = findViewById<ListView>(R.id.lvNotes)

        val notes: List<myActivity> = db.getAllActivities()
        noteAdapter = myActivityAdapter(this, notes)
        lvNotes.adapter = noteAdapter
        lvNotes.onItemClickListener = this

    }

    override fun onResume() {
        super.onResume()

        updateView()
    }

    private fun updateView() {
        noteAdapter!!.notes = db.getAllActivities()
        noteAdapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        println("in der onClick Function in ListActivity.kt");
        val intent = Intent(this, NoteEditActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.menu_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val intent = Intent(this, NoteEditActivity::class.java)
           startActivity(intent)
        }

        return true
    }

     override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, id: Long) {
        val intent = Intent(this, NoteEditActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}