package com.example.androidnotes.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import com.example.androidnotes.R
import kotlinx.android.synthetic.main.edit_activity.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity: Activity() {

    private lateinit var btn_cancel: ImageButton
    private lateinit var btn_save: ImageButton
    private lateinit var edit_text: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)

        val text = intent.getStringExtra(MainActivity.NOTE_TEXT)
        val position = intent.getIntExtra(MainActivity.NOTE_POSITION, 0)

        edit_text = text_note_edit_text
        edit_text.setText(text)

        btn_cancel = btn_edit_cancel
        btn_cancel.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            setResult(MainActivity.RESULT_CANCELED, intent)
            finish()
        })

        btn_save = btn_edit_accept
        btn_save.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra(MainActivity.NOTE_TEXT, edit_text.text.toString())
            intent.putExtra(MainActivity.NOTE_TIMESTAMP, SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(Date()).toString())
            intent.putExtra(MainActivity.NOTE_POSITION, position)

            setResult(MainActivity.RESULT_OK, intent)
            finish()
        })
    }
}