package com.example.androidnotes.activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.example.androidnotes.R
import com.example.androidnotes.TinyDB.TinyDB
import com.example.androidnotes.notes.NotesData
import kotlinx.android.synthetic.main.edit_activity.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity: Activity() {

    private lateinit var btn_cancel: ImageButton
    private lateinit var btn_save: ImageButton
    private lateinit var edit_title: EditText
    private lateinit var edit_text: EditText
    private lateinit var btn_draw: ImageButton
    private lateinit var img_userImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)

        val tinyDB = TinyDB(this)
        var notesData = tinyDB.getObject(MainActivity.NOTES, NotesData::class.java)

        val position = intent.getIntExtra(MainActivity.NOTE_POSITION, 0)
        val title = notesData.notes[position].getTitle()
        val text = notesData.notes[position].getText()
        val bitmap = notesData.notes[position].getImage()

        edit_title = text_note_edit_title
        edit_title.setText(title)
        edit_text = text_note_edit_text
        edit_text.setText(text)
        img_userImage = img_edit_draw
        img_userImage.setImageBitmap(bitmap)

        btn_cancel = btn_edit_cancel
        btn_cancel.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            setResult(MainActivity.RESULT_CANCELED, intent)
            finish()
        })

        btn_save = btn_edit_accept
        btn_save.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra(MainActivity.NOTE_TITLE, edit_title.text.toString())
            intent.putExtra(MainActivity.NOTE_TEXT, edit_text.text.toString())
            intent.putExtra(MainActivity.NOTE_TIMESTAMP, SimpleDateFormat("dd/MM/yyyy\nhh:mm", Locale.getDefault()).format(Date()).toString())
            intent.putExtra(MainActivity.NOTE_POSITION, position)

            setResult(MainActivity.RESULT_OK, intent)
            finish()
        })

        btn_draw = btn_edit_draw
        btn_draw.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DrawingActivity::class.java)
            startActivityForResult(intent, CreateActivity.DRAW_NEW_NOTE)
        })
    }
}