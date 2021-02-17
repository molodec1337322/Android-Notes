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

    private var notes: ArrayList<Note> = ArrayList<Note>()
    private var notesData: NotesData? = null
    private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    private val context: Context = this

    private val PERMISSION_CODE: Int = 1000
    private val NOTES = "notes"

    companion object{
        const val CREATE_NEW_NOTE = 1
        const val EDIT_NOTE = 2
        const val RESULT_OK = 1
        const val RESULT_CANCELED = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        getDataFromDB()

        adapter = NoteAdapter(notes)

        recycler = recycle_list
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter


        button_add = btn_create_new

        button_add.setOnClickListener(View.OnClickListener {
            /*
            notes.add(Note(text, SimpleDateFormat("dd/M/yyyy hh:mm", Locale.getDefault()).format(Date())))
            adapter.notifyItemInserted(notes.size - 1)
            */

            val intent = Intent(context, CreateActivity::class.java)
            startActivityForResult(intent, CREATE_NEW_NOTE)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CREATE_NEW_NOTE && resultCode == RESULT_OK){
            val noteText = data?.extras?.getString(CreateActivity.NOTE_TEXT)
            val noteTimeStamp = data?.extras?.getString(CreateActivity.NOTE_TIMESTAMP)

            notes.add(Note(noteText!!, noteTimeStamp!!))
            adapter!!.notifyItemChanged(notes.size - 1)

            putDataInDB()

            Toast.makeText(context, "Заметка создана", Toast.LENGTH_SHORT).show()
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

    /*
    fun addNewNote(){
        val noteText = intent.extras?.getString(CreateActivity.NOTE_TEXT)
        val noteTimeStamp = intent.extras?.getString(CreateActivity.NOTE_TIMESTAMP)

        if(noteText)
    }
    */

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
}