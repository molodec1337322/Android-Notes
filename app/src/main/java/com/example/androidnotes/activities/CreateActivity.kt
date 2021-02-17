package com.example.androidnotes.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import com.example.androidnotes.R
import kotlinx.android.synthetic.main.create_activity.*
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity: Activity() {

    private lateinit var btn_save: ImageButton
    private lateinit var btn_cancel: ImageButton
    private lateinit var text_note: EditText

    companion object{
        val NOTE_TIMESTAMP = "note_timestamp"
        val NOTE_TEXT = "note_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_activity)

        text_note = text_note_text

        btn_save = btn_create_new_accept
        btn_save.setOnClickListener(View.OnClickListener {
            if(text_note.text.toString() != "" ){
                val intent = Intent(this, MainActivity::class.java)

                intent.putExtra(NOTE_TEXT, text_note.text.toString())
                intent.putExtra(NOTE_TIMESTAMP, SimpleDateFormat("dd/M/yyyy hh:mm", Locale.getDefault()).format(Date()).toString())

                setResult(MainActivity.RESULT_OK, intent)
                finish()
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                setResult(MainActivity.RESULT_CANCELED, intent)
                finish()
            }
        })

        btn_cancel = btn_create_new_cancel
        btn_cancel.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            setResult(MainActivity.RESULT_CANCELED, intent)
            finish()
            //startActivity(intent)
        })
    }
}