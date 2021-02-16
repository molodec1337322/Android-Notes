package com.example.androidnotes.activities

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = recycle_list
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        button_add = btn_create_new

        button_add.setOnClickListener(View.OnClickListener {
            val text = text_to_add.text.toString()
            notes.add(Note(text, SimpleDateFormat("dd/M/yyyy hh:mm", Locale.getDefault()).format(Date())))
            adapter.notifyItemInserted(notes.size - 1)
        })
    }
}