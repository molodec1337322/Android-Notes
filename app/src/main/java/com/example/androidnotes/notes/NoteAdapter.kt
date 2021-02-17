package com.example.androidnotes.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidnotes.R

class NoteAdapter(val notes: List<Note>): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object: RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)){}
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val name = holder.itemView.findViewById<TextView>(R.id.text_note_name)
        name.setText(notes.get(position).getText())
        val timestamp = holder.itemView.findViewById<TextView>(R.id.text_timestamp)
        timestamp.setText(notes.get(position).getTimestamp())
    }
}