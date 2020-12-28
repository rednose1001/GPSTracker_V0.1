package com.aichbauer.gpstracker_i

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        // Database properties
        private const val DATABASE_NAME = "gpstracker"
        private const val DATABASE_TABLE_NAME = "activities"
        private const val DATABASE_VERSION = 1

        // Database table column names
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"
        private const val KEY_TIMESTAMP = "timestamp"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        // Database create table statement
        private const val CREATE_TABLE = ("""CREATE TABLE $DATABASE_TABLE_NAME(
                $KEY_ID INTEGER PRIMARY KEY,
                $KEY_TIMESTAMP INT,
                $KEY_TITLE TEXT,
                $KEY_MESSAGE TEXT,
                $KEY_LATITUDE FLOAT,
                $KEY_LONGITUDE FLOAT
            )""")

        // Database cursor array
        private val CURSOR_ARRAY = arrayOf(
            KEY_ID,
            KEY_TIMESTAMP,
            KEY_TITLE,
            KEY_MESSAGE,
            KEY_LATITUDE,
            KEY_LONGITUDE
        )

        // Drop table statement
        private const val DROP_TABLE = "DROP TABLE IF EXISTS $DATABASE_TABLE_NAME"

        // Database select all statement
        private const val SELECT_ALL = "SELECT * FROM $DATABASE_TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(DROP_TABLE)
        db.execSQL(CREATE_TABLE)
    }

    // Get all notes from database
   fun getAllActivities(): List<myActivity> {
        val notes = ArrayList<myActivity>()
        val cursor = readableDatabase.rawQuery(SELECT_ALL, null)
        cursor.moveToFirst().run {
            do {
                cursorToActivity(cursor)?.let {
                    notes.add(it)
                }
            } while (cursor.moveToNext())
        }

        readableDatabase.close()

        return notes
    }

    // Insert note into database
    fun insertNote(note: myActivity): Long {
        val values = noteToContentValues(note)

        return writableDatabase.insert(DATABASE_TABLE_NAME, null, values)
    }

    // Get single note from database
    fun getNote(id: Long): myActivity? {
        val note: myActivity?
        val cursor = readableDatabase.query(
            DATABASE_TABLE_NAME, CURSOR_ARRAY, "$KEY_ID=?",
            arrayOf(id.toString()), null, null, null, null
        )

        cursor.moveToFirst()
        note = cursorToActivity(cursor)
        cursor.close()

        return note
    }

    private fun cursorToActivity(cursor: Cursor):myActivity? {
        var note: myActivity? = null
        if (cursor?.count == 0) return null
        cursor.run {
            note = myActivity(
                getLong(getColumnIndex(KEY_ID)),
                getLong(getColumnIndex(KEY_TIMESTAMP)),
                getString(getColumnIndex(KEY_TITLE)),
                getString(getColumnIndex(KEY_MESSAGE)),
                getDouble(getColumnIndex(KEY_LATITUDE)),
                getDouble(getColumnIndex(KEY_LONGITUDE))
            )
        }

        return note
    }

    // Update single note
    fun updateNote(note: myActivity): Int {
        return writableDatabase.update(DATABASE_TABLE_NAME,
            noteToContentValues(note),
            "$KEY_ID=?",
            arrayOf(note.id.toString()))
    }

    // Create new ContentValues object from Note
    private fun noteToContentValues(note: myActivity): ContentValues {
        val values = ContentValues()

        values.put(KEY_TIMESTAMP, note.timestamp)
        values.put(KEY_TITLE, note.title)
        values.put(KEY_MESSAGE, note.message)
        values.put(KEY_LATITUDE, note.latitude)
        values.put(KEY_LONGITUDE, note.longitude)

        return values
    }

    // Delete single note
    fun deleteNote(note: myActivity) {
        writableDatabase.delete(
            DATABASE_TABLE_NAME,
            "$KEY_ID=?",
            arrayOf(note.id.toString())
        )
    }
}