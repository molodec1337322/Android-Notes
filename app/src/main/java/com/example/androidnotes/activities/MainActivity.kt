package com.example.androidnotes.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.androidnotes.R
import com.example.androidnotes.notes.Note
import com.example.androidnotes.notes.NoteAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var button_add: ImageButton

    private var notes: ArrayList<Note> = ArrayList<Note>()
    private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = NoteAdapter(notes)

    private val context: Context = this

    companion object{
        const val CREATE_NEW_NOTE = 1
        const val EDIT_NOTE = 2
        const val RESULT_OK = 1
        const val RESULT_CANCELED = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            adapter.notifyItemChanged(notes.size - 1)
        }
    }

    /*
    fun addNewNote(){
        val noteText = intent.extras?.getString(CreateActivity.NOTE_TEXT)
        val noteTimeStamp = intent.extras?.getString(CreateActivity.NOTE_TIMESTAMP)

        if(noteText)
    }
    */

    fun putInDB(){

    }

    fun getFromDB(){

    }
}