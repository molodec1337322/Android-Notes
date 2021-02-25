package com.example.androidnotes.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.example.androidnotes.R
import kotlinx.android.synthetic.main.create_activity.*
import kotlinx.android.synthetic.main.items.*
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity: Activity() {

    private lateinit var btn_save: ImageButton
    private lateinit var btn_cancel: ImageButton
    private lateinit var title_note: EditText
    private lateinit var text_note: EditText
    private lateinit var btn_draw: ImageButton
    private lateinit var img_userImage: ImageView

    private var noteImage: Bitmap? = null

    companion object{
        const val DRAW_NEW_NOTE = 4
        const val RESULT_OK = 1
        const val RESULT_CANCELED = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_activity)

        title_note = text_note_title
        text_note = text_note_text

        btn_save = btn_create_new_accept
        btn_save.setOnClickListener(View.OnClickListener {
            if(text_note.text.toString() != "" || text_note_title.text.toString() != ""){
                val intent = Intent(this, MainActivity::class.java)

                intent.putExtra(MainActivity.NOTE_TITLE, text_note_title.text.toString())
                intent.putExtra(MainActivity.NOTE_TEXT, text_note.text.toString())
                intent.putExtra(MainActivity.NOTE_TIMESTAMP, SimpleDateFormat("dd/MM/yyyy\nhh:mm", Locale.getDefault()).format(Date()).toString())
                when(noteImage){
                    null -> intent.putExtra(MainActivity.NOTE_IMAGE_AVAILABLE, false)
                    else -> intent.putExtra(MainActivity.NOTE_IMAGE_AVAILABLE, true)
                }

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

        btn_draw = btn_create_new_draw
        btn_draw.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DrawingActivity::class.java)
            startActivityForResult(intent, DRAW_NEW_NOTE)
        })

        img_userImage = img_new_draw
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == DRAW_NEW_NOTE && resultCode == RESULT_OK){
            /*
            noteImage = data?.extras?.getParcelable(DrawingActivity.BITMAP_TO_IMPORT)
            */
            noteImage = BitmapFactory.decodeStream(this.openFileInput(DrawingActivity.BITMAP_TO_IMPORT))
            img_userImage.setImageBitmap(noteImage)
        }
        else if(requestCode == DRAW_NEW_NOTE && resultCode == RESULT_CANCELED){
            if(noteImage != null){
                noteImage!!.recycle()
            }
            noteImage = null
        }
    }
}