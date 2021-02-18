package com.example.androidnotes.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.androidnotes.R
import com.example.androidnotes.TinyDB.TinyDB
import com.example.androidnotes.extensions.setActive
import com.example.androidnotes.extensions.setInactive
import com.example.androidnotes.notes.Note
import com.example.androidnotes.notes.NoteAdapter
import com.example.androidnotes.notes.NotesData
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var button_add: ImageButton
    private lateinit var button_reply: ImageButton

    private var notes: MutableList<Note> = mutableListOf<Note>()
    private var deletedNotes: Stack<Pair<Note, Int>> = Stack()
    private var notesData: NotesData? = null
    private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    private val context: Context = this

    private val PERMISSION_CODE: Int = 1000

    companion object{
        const val CREATE_NEW_NOTE = 1
        const val EDIT_NOTE = 2

        const val RESULT_OK = 1
        const val RESULT_CANCELED = -1

        const val NOTES = "notes"
        const val NOTE_TEXT = "note_text"
        const val NOTE_TITLE = "note_title"
        const val NOTE_TIMESTAMP = "note_timestamp"
        const val NOTE_POSITION = "note_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        getDataFromDB()

        adapter = NoteAdapter(notes, deletedNotes, context, { checkForDeletedNotes() })

        recycler = recycle_list
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter


        button_add = btn_create_new
        button_reply = btn_reply

        checkForDeletedNotes()

        button_add.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, CreateActivity::class.java)
            startActivityForResult(intent, CREATE_NEW_NOTE)
        })

        button_reply.setOnClickListener(View.OnClickListener {
            if(!deletedNotes.empty()){
                val (note, pos) = deletedNotes.pop()
                notes.add(pos, note)
                adapter!!.notifyDataSetChanged()
                putDataInDB()
                checkForDeletedNotes()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CREATE_NEW_NOTE && resultCode == RESULT_OK){
            val noteTitle = data?.extras?.getString(NOTE_TITLE)
            val noteText = data?.extras?.getString(NOTE_TEXT)
            val noteTimeStamp = data?.extras?.getString(NOTE_TIMESTAMP)

            notes.add(Note(noteTitle!!, noteText!!, noteTimeStamp!!))
            adapter!!.notifyItemChanged(notes.size - 1)

            putDataInDB()

            Toast.makeText(context, "Заметка создана", Toast.LENGTH_SHORT).show()
        }
        else if(requestCode == EDIT_NOTE && resultCode == RESULT_OK){
            val noteTitle = data?.extras?.getString(NOTE_TITLE)
            val noteText = data?.extras?.getString(NOTE_TEXT)
            val noteTimestamp = data?.extras?.getString(NOTE_TIMESTAMP)
            val notePos = data?.extras?.getInt(NOTE_POSITION)

            val note = notes[notePos!!]
            note.setTitle(noteTitle!!)
            note.setText(noteText!!)
            note.setTimestamp("Изменено $noteTimestamp")

            adapter!!.notifyItemChanged(notePos)

            putDataInDB()

            Toast.makeText(context, "Заметка изменена", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun checkPermissions(){
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        for (permission in permissions){
            if(checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
                requestPermissions(permissions, PERMISSION_CODE)
            }
        }
    }

    fun putDataInDB(){
        val tinyDB = TinyDB(context)
        tinyDB.putObject(NOTES, NotesData(notes))
    }

    fun getDataFromDB(){
        val tinyDB = TinyDB(context)
        notesData = tinyDB.getObject(NOTES, NotesData::class.java)

        if(notesData != null){
            notes = notesData!!.notes
        }
    }

    fun checkForDeletedNotes(){
        if(deletedNotes.empty()){
            button_reply.setInactive()
        }
        else{
            button_reply.setActive()
        }
    }
}