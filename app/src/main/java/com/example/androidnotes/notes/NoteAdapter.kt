package com.example.androidnotes.notes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
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
        holder.noteTitle.text = notes[position].getTitle()
        holder.noteText.text = notes[position].getText()
        holder.timeStamp.text = notes[position].getTimestamp()
        //holder.noteImage.setImageBitmap(notes[position].getImage())


        val image = notes[position].getImage()
        //holder.noteImage.setImageBitmap(image)

        if(image != null){
            holder.noteImage.setImageBitmap(image)
            //image.recycle()
        }
        else{
            holder.noteImage.setImageBitmap(null)
            holder.noteImage.maxHeight = 0
            holder.noteImage.visibility = ImageView.INVISIBLE
        }



        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, EditActivity::class.java)

            intent.putExtra(MainActivity.NOTE_POSITION, position)

            val tempActivity = context as Activity

            tempActivity.startActivityForResult(intent, MainActivity.EDIT_NOTE)
        })
    }

    class NoteHolder(noteView: View): RecyclerView.ViewHolder(noteView){
        val noteTitle = noteView.findViewById<TextView>(R.id.title_note)
        val noteText = noteView.findViewById<TextView>(R.id.text_note)
        val timeStamp = noteView.findViewById<TextView>(R.id.text_timestamp)
        val noteImage = noteView.findViewById<ImageView>(R.id.img_note_image)
    }


}