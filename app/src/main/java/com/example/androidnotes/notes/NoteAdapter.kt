package com.example.androidnotes.notes

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidnotes.R
import com.example.androidnotes.TinyDB.TinyDB
import com.example.androidnotes.activities.EditActivity
import com.example.androidnotes.activities.MainActivity
import java.util.*

class NoteAdapter(
    val notes: MutableList<Note>,
    val context: Context
): RecyclerView.Adapter<NoteAdapter.NoteHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val noteView = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return NoteHolder(noteView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteHolder, position: Int) {

        /*
        val noteTitle = holder.itemView.findViewById<TextView>(R.id.title_note)
        noteTitle.setText(notes.get(position).getTitle())
        val noteText = holder.itemView.findViewById<TextView>(R.id.text_note)
        noteText.setText(notes.get(position).getText())
        val timestamp = holder.itemView.findViewById<TextView>(R.id.text_timestamp)
        timestamp.setText(notes.get(position).getTimestamp())
         */

        holder.noteTitle.text = notes[position].getTitle()
        holder.noteText.text = notes[position].getText()
        holder.timeStamp.text = notes[position].getTimestamp()

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, EditActivity::class.java)

            intent.putExtra(MainActivity.NOTE_TITLE, notes[position].getTitle())
            intent.putExtra(MainActivity.NOTE_TEXT, notes[position].getText())
            intent.putExtra(MainActivity.NOTE_POSITION, position)

            val tempActivity = context as Activity

            tempActivity.startActivityForResult(intent, MainActivity.EDIT_NOTE)
        })
    }

    private fun putDataInDB(){
        val tinyDB = TinyDB(context)
        tinyDB.putObject(MainActivity.NOTES, NotesData(notes))
    }

    class NoteHolder(val noteView: View): RecyclerView.ViewHolder(noteView){
        val noteTitle = noteView.findViewById<TextView>(R.id.title_note)
        val noteText = noteView.findViewById<TextView>(R.id.text_note)
        val timeStamp = noteView.findViewById<TextView>(R.id.text_timestamp)
    }
}